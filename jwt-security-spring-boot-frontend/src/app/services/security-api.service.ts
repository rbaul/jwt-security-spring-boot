import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { User } from '../security/models/user';
import { Observable } from 'rxjs';
import { SignUp } from '../security/models/singup';
import { UserAuth } from '../security/models/user-auth';


const API_URL = '/api/security/';
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

  public login(user: User): Observable<UserAuth> {
    return this.http.post<UserAuth>(API_URL + 'login', user, httpOptions);
  }

}
