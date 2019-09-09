import { HttpParams } from '@angular/common/http';

export class ApiUtils {
    public static getPagableHttpParams(pageSize: number, pageNumber: number,
        sort: string[], filter: string): HttpParams {
        let httpParams = new HttpParams();
        httpParams = pageNumber != null ? httpParams.append('page', pageNumber.toString()) : httpParams;
        httpParams = pageSize ? httpParams.append('size', pageSize.toString()) : httpParams;
        httpParams = filter ? httpParams.append('filter', filter) : httpParams;
        sort.forEach(sortParam => httpParams = httpParams.append('sort', sortParam));
        return httpParams;
    }
}
