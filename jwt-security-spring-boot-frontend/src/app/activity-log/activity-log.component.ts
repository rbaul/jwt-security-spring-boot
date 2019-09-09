import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, MatSnackBar, MatSort } from '@angular/material';
import { fromEvent, merge } from 'rxjs';
import { debounceTime, distinctUntilChanged, tap } from 'rxjs/operators';
import { GenericDataSource } from '../app-security-module/models/generic.datasource';
import { DialogService } from '../app-security-module/shared/common-dialogs/dialog.service';
import { ActivityLog, ActivityLogStatus } from './models/activity-log';
import { ActivityLogApiService } from './services/activity-log-api.service';

@Component({
  selector: 'app-activity-log',
  templateUrl: './activity-log.component.html',
  styleUrls: ['./activity-log.component.scss']
})
export class ActivityLogComponent implements OnInit, AfterViewInit {

  @ViewChild(MatPaginator) paginator: MatPaginator;

  @ViewChild(MatSort) sort: MatSort;

  @ViewChild('input') input: ElementRef;

  dataSource: GenericDataSource<ActivityLog>;

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['id', 'username', 'action', 'time', 'status', 'actions'];

  pageSize = 10;
  pageSizeOptions = [10, 50, 100];

  constructor(
    private activityLogApiService: ActivityLogApiService,
    private dialogService: DialogService,
    private _snackBar: MatSnackBar
  ) { }

  ngOnInit() {
    this.dataSource = new GenericDataSource(this.activityLogApiService);
    this.dataSource.loadContent(this.pageSize, 0, ['time,desc'], '');
  }

  ngAfterViewInit(): void {
    // If the user changes the sort order, reset back to the first page.
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    fromEvent(this.input.nativeElement, 'keyup')
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        tap(() => {
          // reset back to the first page.
          this.paginator.pageIndex = 0;

          this.loadActivityLogContent();
        })
      )
      .subscribe();

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        tap(() => this.loadActivityLogContent())
      ).subscribe();
  }

  loadActivityLogContent() {
    this.dataSource.loadContent(
      this.paginator.pageSize,
      this.paginator.pageIndex,
      this.sort.active && [this.sort.active + ',' + this.sort.direction],
      this.input.nativeElement.value);
  }

  isActivityLogSuccess(status: ActivityLogStatus): boolean {
    return status === ActivityLogStatus.SUCCESS;
  }

  delete(activityLog: ActivityLog) {
    this.dialogService.openConfirmDialog({
      message: 'Are you sure to delete this activity log?',
      okName: 'Delete',
      title: 'Deletion'
    }).afterClosed().subscribe(
      data => {
        if (data) {
          this.activityLogApiService.deleteActivityLog(activityLog.id)
            .subscribe(response => {
              this._snackBar.open('Activity Log has been deleted successfully');
              this.loadActivityLogContent();
            });
        }
      }
    );
  }

}
