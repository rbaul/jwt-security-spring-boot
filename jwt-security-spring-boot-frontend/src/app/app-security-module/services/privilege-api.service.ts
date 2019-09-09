import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PrivilegeResponseDto } from '../models/role';
import { PageableApiService } from './pageable-api-service';


const API_URL = '/api/privileges';
const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
  })
};

@Injectable({
  providedIn: 'root'
})
export class PrivilegeApiService extends PageableApiService<PrivilegeResponseDto> {

  constructor(
    http: HttpClient
  ) {
    super(http, API_URL);
  }

  getPrivileges(): Observable<PrivilegeResponseDto[]> {
    return this.http.get<PrivilegeResponseDto[]>(API_URL, httpOptions);
  }

  getPrivilege(privilegeId: number): Observable<PrivilegeResponseDto> {
    return this.http.get<PrivilegeResponseDto>(API_URL + '/' + privilegeId, httpOptions);
  }

}
