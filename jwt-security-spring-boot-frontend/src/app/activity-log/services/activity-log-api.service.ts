import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PageableApiService } from 'src/app/app-security-module/services/pageable-api-service';
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
export class ActivityLogApiService extends PageableApiService<ActivityLog> {

  constructor(
    http: HttpClient
  ) {
    super(http, API_URL);
  }

  getActivityLog(activityLogId: number): Observable<ActivityLog> {
    return this.http.get<ActivityLog>(API_URL + '/' + activityLogId, httpOptions);
  }

  deleteActivityLog(activityLogId: number): Observable<void> {
    return this.http.delete<void>(API_URL + '/' + activityLogId, httpOptions);
  }

}
