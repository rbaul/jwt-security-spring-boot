import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';

import { JwtHelperService } from '@auth0/angular-jwt';
import { UserAuth, UserAuthResponse } from '../models/user-auth';
import { SecurityApiService } from '../services/security-api.service';
import { User } from '../models/user';
import { Token } from '../models/token';

@Injectable({
    providedIn: 'root'
})
export class SecurityService {
  securityObject: UserAuth = new UserAuth();

  private loggedIn: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  private loggedUser: BehaviorSubject<string> = new BehaviorSubject<string>('');

  constructor(
    private securityApi: SecurityApiService
  ) {
    const token: string = localStorage.getItem('bearerToken');

    const securityObject: UserAuth = this.getTokenObject(token);
    if (securityObject) {
      Object.assign(this.securityObject, securityObject);
      this.loginProccess();
    } else {
      this.logoutProccess();
    }

  }

  get loggedUserIn() {
    return this.loggedUser.asObservable();
  }

  get isLoggedIn() {
    return this.loggedIn.asObservable();
  }

  login(user: User): Observable<UserAuthResponse> {
    // Initialize security object
    this.resetSecurityObject();

    return this.securityApi.login(user).pipe(
        tap(resp => {
          const tokenObject: UserAuth = this.getTokenObject(resp.bearerToken);

          // Use object assign to update the current object
          // NOTE: Don't create a new AppUserAuth object
          //       because that destroys all references to object
          Object.assign(this.securityObject, tokenObject);

          // Store into local storage
          localStorage.setItem('bearerToken', this.securityObject.bearerToken);
          this.loginProccess();
        })
    );
  }

  loginProccess(): void {
    this.loggedUser.next(this.securityObject.username);
    this.loggedIn.next(true);
  }

  logoutProccess(): void {
    this.resetSecurityObject();
    this.loggedUser.next('');
    this.loggedIn.next(false);
  }

  logout(): void {
    this.securityApi.logout().subscribe(
      response => {
      },
      error => console.error(error));
    this.logoutProccess();
  }

  resetSecurityObject(): void {
    this.securityObject.username = '';
    this.securityObject.bearerToken = '';
    this.securityObject.authenticated = false;
    this.securityObject.roles = [];
    this.securityObject.privileges = [];

    localStorage.removeItem('bearerToken');
  }

  public hasPrivilege(privileges: String[]): boolean {
    return privileges ? this.securityObject.privileges.some(privilege => privileges.indexOf(privilege) > -1) : true;
  }

  private getTokenObject(tokenString: string): UserAuth {

    try {
      const jwtParser = new JwtHelperService();
      const tokenObject: Token = jwtParser.decodeToken(tokenString);

      if (tokenObject) {
        const securityObject: UserAuth = new UserAuth();

        securityObject.username = tokenObject.sub;
        securityObject.bearerToken = tokenString;
        securityObject.authenticated = true;
        securityObject.roles = tokenObject.roles;
        securityObject.privileges = tokenObject.privileges;
        securityObject.exp = tokenObject.exp;

        return securityObject;
      } else {
        return undefined;
      }
    } catch (e) {
      return undefined;
    }
  }

}
