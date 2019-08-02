import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PrivilegeResponseDto } from '../models/role';


const API_URL = '/api/privileges';
const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
  })
};

@Injectable({
  providedIn: 'root'
})
export class PrivilegeApiService {

  constructor(
    private http: HttpClient
  ) { }

  getPrivileges(): Observable<PrivilegeResponseDto[]> {
    return this.http.get<PrivilegeResponseDto[]>(API_URL, httpOptions);
  }

  getPrivilege(privilegeId: number): Observable<PrivilegeResponseDto> {
    return this.http.get<PrivilegeResponseDto>(API_URL + '/' + privilegeId, httpOptions);
  }
}
