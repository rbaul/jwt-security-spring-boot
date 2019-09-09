import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RoleRequestDto, RoleResponseDto } from '../models/role';
import { PageableApiService } from './pageable-api-service';


const API_URL = '/api/roles';
const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
  })
};

@Injectable({
  providedIn: 'root'
})
export class RoleApiService extends PageableApiService<RoleResponseDto> {

  constructor(
    http: HttpClient
  ) {
    super(http, API_URL);
  }

  getAllRoles(): Observable<RoleResponseDto[]> {
    return this.http.get<RoleResponseDto[]>(API_URL, httpOptions);
  }

  public create(role: RoleRequestDto): Observable<RoleResponseDto> {
    return this.http.post<RoleResponseDto>(API_URL, role, httpOptions);
  }

  public update(roleId: number, request: RoleRequestDto): Observable<RoleResponseDto> {
    return this.http.put<RoleResponseDto>(API_URL + '/' + roleId, request, httpOptions);
  }

  deleteRole(roleId: number): Observable<void> {
    return this.http.delete<void>(API_URL + '/' + roleId, httpOptions);
  }

  getRole(roleId: number): Observable<RoleResponseDto> {
    return this.http.get<RoleResponseDto>(API_URL + '/' + roleId, httpOptions);
  }

}
