import { DataSource } from '@angular/cdk/table';
import { BehaviorSubject, of, Observable } from 'rxjs';
import { catchError, finalize } from 'rxjs/operators';
import { CollectionViewer } from '@angular/cdk/collections';

import { Page } from 'src/app/app-security-module/models/page';

export abstract class PageableDataSource<T> extends DataSource<T> {

    private contentSubject = new BehaviorSubject<T[]>([]);

    private loadingSubject = new BehaviorSubject<boolean>(false);

    private totalElementsSubject = new BehaviorSubject<number>(0);

    public loading$ = this.loadingSubject.asObservable();

    public totalElements$ = this.totalElementsSubject.asObservable();

    loadContent(pageSize = 10000, pageIndex = 0, sort = [], filter = '') {
        this.loadingSubject.next(true);
        this.totalElementsSubject.next(0);

        this.getPageableContent(pageSize, pageIndex, sort, filter)
            .pipe(
                catchError(() => of(new Page<T>())),
                finalize(() => this.loadingSubject.next(false))
            )
            .subscribe(data => {
                this.totalElementsSubject.next(data.totalElements);
                this.contentSubject.next(data.content);
            });
    }

    abstract getPageableContent(pageSize: number, pageIndex: number, sort: string[], filter: string): Observable<Page<T>>;

    connect(collectionViewer: CollectionViewer): Observable<T[]> {
        return this.contentSubject.asObservable();
    }

    disconnect(collectionViewer: CollectionViewer): void {
        this.contentSubject.complete();
        this.loadingSubject.complete();
        this.totalElementsSubject.complete();
    }

}
