import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { User } from '../models/user';
import { Observable } from 'rxjs';
import { UserAuthResponse } from '../models/user-auth';


const API_URL = '/api/security';
const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
  })
};

@Injectable({
  providedIn: 'root'
})
export class SecurityApiService {

  constructor(
    private http: HttpClient
  ) { }

  public login(user: User): Observable<UserAuthResponse> {
    return this.http.post<UserAuthResponse>(API_URL + '/login', user, httpOptions);
  }

  public logout(): Observable<void> {
    return this.http.post<void>(API_URL + '/logout', httpOptions);
  }

}
