import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RoleResponseDto, RoleRequestDto } from '../models/role';


const API_URL = '/api/roles';
const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
  })
};

@Injectable({
  providedIn: 'root'
})
export class RoleApiService {

  constructor(
    private http: HttpClient
  ) { }

  getAllRoles(): Observable<RoleResponseDto[]> {
    return this.http.get<RoleResponseDto[]>(API_URL, httpOptions);
  }

  public create(role: RoleRequestDto): Observable<RoleResponseDto> {
    return this.http.post<RoleResponseDto>(API_URL , role, httpOptions);
  }

  public update(roleId: number, request: RoleRequestDto): Observable<RoleResponseDto> {
    return this.http.put<RoleResponseDto>(API_URL + '/' + roleId, request, httpOptions);
  }

  deleteRole(id: number): Observable<void> {
    return this.http.delete<void>(API_URL + '/' + id.toString(), httpOptions);
  }

  getRole(roleId: number): Observable<RoleResponseDto> {
    return this.http.get<RoleResponseDto>(API_URL + '/' + roleId, httpOptions);
  }
}
