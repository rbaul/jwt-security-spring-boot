import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { User } from '../security/models/user';
import { Observable } from 'rxjs';
import { SignUp } from '../security/models/singup';
import { UserAuth } from '../security/models/user-auth';
import { Page } from '../models/page';


const API_URL = '/api/users/';
const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
  })
};

@Injectable({
  providedIn: 'root'
})
export class UserApiService {

  constructor(
    private http: HttpClient
  ) { }

  public create(singup: SignUp): Observable<any> {
    return this.http.post(API_URL + 'create', singup, httpOptions);
  }

  public logout(user: User): Observable<any> {
    return this.http.post(API_URL, user, httpOptions);
  }

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(API_URL, httpOptions);
  }

  getPageableUsers(): Observable<Page<User>> {
    return this.http.get<Page<User>>(API_URL + 'pageable');
  }

  getUser(userId: number): Observable<User> {
    return this.http.get<User>(API_URL + userId, httpOptions);
  }
}
