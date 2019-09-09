import { Observable } from 'rxjs';
import { Page } from 'src/app/app-security-module/models/page';
import { PageableApiService } from '../services/pageable-api-service';
import { PageableDataSource } from '../services/pageable.datasource';

export class GenericDataSource<T> extends PageableDataSource<T> {

    constructor(private pageableApiService: PageableApiService<T>) {
        super();
    }

    getPageableContent(pageSize: number, pageIndex: number, sort: string[], filter: string): Observable<Page<T>> {
        return this.pageableApiService.search(pageSize, pageIndex, sort, filter);
    }

}
