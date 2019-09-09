import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from '../models/page';
import { ApiUtils } from './api-utils';

export abstract class PageableApiService<T> {

    constructor(
        protected http: HttpClient,
        public apiUrl: string
    ) {
    }

    search(pageSize: number, pageNumber: number,
        sort: string[], filter: string): Observable<Page<T>> {
        const httpParams = ApiUtils.getPagableHttpParams(pageSize, pageNumber, sort, filter);
        return this.http.get<Page<T>>(this.apiUrl + '/search', { params: httpParams });
    }
}
