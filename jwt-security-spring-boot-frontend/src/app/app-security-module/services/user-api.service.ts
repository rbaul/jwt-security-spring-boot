import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { User, UserResponseDto, UserRequestDto } from '../models/user';
import { Observable } from 'rxjs';
import { Page } from '../models/page';


const API_URL = '/api/users';
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

  public create(singup: UserRequestDto): Observable<UserResponseDto> {
    return this.http.post<UserResponseDto>(API_URL , singup, httpOptions);
  }

  public update(userId: number, request: UserRequestDto): Observable<UserResponseDto> {
    return this.http.put<UserResponseDto>(API_URL + '/' + userId, request, httpOptions);
  }

  public logout(user: User): Observable<any> {
    return this.http.post(API_URL, user, httpOptions);
  }

  getAllUsers(): Observable<UserResponseDto[]> {
    return this.http.get<UserResponseDto[]>(API_URL + '/all', httpOptions);
  }

  getPageableUsers(): Observable<Page<UserResponseDto>> {
    return this.http.get<Page<UserResponseDto>>(API_URL);
  }

  getUser(userId: number): Observable<UserResponseDto> {
    return this.http.get<UserResponseDto>(API_URL + '/' + userId, httpOptions);
  }

  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(API_URL + '/' + id.toString(), httpOptions);
  }
}
