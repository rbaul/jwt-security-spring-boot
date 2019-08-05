import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from '../../app-security-module/models/page';
import { ActivityLog } from '../models/activity-log';

const API_URL = '/api/activity-logs';
const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
  })
};

@Injectable({
  providedIn: 'root'
})
export class ActivityLogApiService {

  constructor(
    private http: HttpClient
  ) { }

  getActivityLog(activityLogId: number): Observable<ActivityLog> {
    return this.http.get<ActivityLog>(API_URL + '/' + activityLogId, httpOptions);
  }

  deleteActivityLog(activityLogId: number): Observable<void> {
    return this.http.delete<void>(API_URL + '/' + activityLogId, httpOptions);
  }

  getPageableActivityLog(): Observable<Page<ActivityLog>> {
    return this.http.get<Page<ActivityLog>>(API_URL);
  }
}
