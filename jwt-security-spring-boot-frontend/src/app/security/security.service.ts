import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';

import { User } from './models/user';
import { UserAuth } from './models/user-auth';
import { SecurityApiService } from '../services/security-api.service';

@Injectable({
    providedIn: 'root'
})
export class SecurityService {
  securityObject: UserAuth = new UserAuth();

  private loggedIn: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  private loggedUser: BehaviorSubject<string> = new BehaviorSubject<string>('');

  constructor(private securityApi: SecurityApiService) {
    const token = localStorage.getItem('bearerToken');
    const username = localStorage.getItem('username');

    if (token && username) {
        this.securityObject.username = username;
        this.securityObject.bearerToken = token;
        this.securityObject.authenticated = true;
        this.securityObject.claims = [];
        this.loggedUser.next(this.securityObject.username);
        this.loggedIn.next(true);
    }
  }

  get loggedUserIn() {
    return this.loggedUser.asObservable();
  }

  get isLoggedIn() {
    return this.loggedIn.asObservable();
  }

  login(user: User): Observable<UserAuth> {
    // Initialize security object
    this.resetSecurityObject();

    return this.securityApi.login(user).pipe(
        tap(resp => {
            // Use object assign to update the current object
            // NOTE: Don't create a new AppUserAuth object
            //       because that destroys all references to object
            Object.assign(this.securityObject, resp);
            // Store into local storage
            localStorage.setItem('bearerToken', this.securityObject.bearerToken);
            localStorage.setItem('username', this.securityObject.username);
            this.loggedUser.next(this.securityObject.username);
            this.loggedIn.next(true);
    }));
  }

  logout(): void {
    this.resetSecurityObject();
    this.loggedUser.next('');
    this.loggedIn.next(false);
  }

  resetSecurityObject(): void {
    this.securityObject.username = '';
    this.securityObject.bearerToken = '';
    this.securityObject.authenticated = false;

    this.securityObject.claims = [];

    localStorage.removeItem('bearerToken');
    localStorage.removeItem('username');
  }

  // This method can be called a couple of different ways
  // *hasClaim="'claimType'"  // Assumes claimValue is true
  // *hasClaim="'claimType:value'"  // Compares claimValue to value
  // *hasClaim="['claimType1','claimType2:value','claimType3']"
  hasClaim(claimType: any, claimValue?: any) {
    let ret: boolean = false;

    // See if an array of values was passed in.
    if (typeof claimType === 'string') {
      ret = this.isClaimValid(claimType, claimValue);
    } else {
      const claims: string[] = claimType;
      if (claims) {
        for (let index = 0; index < claims.length; index++) {
          ret = this.isClaimValid(claims[index]);
          // If one is successful, then let them in
          if (ret) {
            break;
          }
        }
      }
    }

    return ret;
  }


  private isClaimValid(claimType: string, claimValue?: string): boolean {
    let ret: boolean = false;
    let auth: UserAuth = null;

    // Retrieve security object
    auth = this.securityObject;
    if (auth) {
      // See if the claim type has a value
      // *hasClaim="'claimType:value'"
      if (claimType.indexOf(':') >= 0) {
        const words: string[] = claimType.split(':');
        claimType = words[0].toLowerCase();
        claimValue = words[1];
      } else {
        claimType = claimType.toLowerCase();
        // Either get the claim value, or assume 'true'
        claimValue = claimValue ? claimValue : 'true';
      }
      // Attempt to find the claim
      ret = auth.claims.find(c =>
        c.claimType.toLowerCase() === claimType &&
        c.claimValue === claimValue) != null;
    }

    return ret;
  }
}