import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { MatPaginator, MatSort, MatTableDataSource, MatSnackBar } from '@angular/material';
import { ActivityLog, ActivityLogStatus } from './models/activity-log';
import { DialogService } from '../app-security-module/shared/common-dialogs/dialog.service';
import { ActivityLogApiService } from './services/activity-log-api.service';

@Component({
  selector: 'app-activity-log',
  templateUrl: './activity-log.component.html',
  styleUrls: ['./activity-log.component.scss']
})
export class ActivityLogComponent implements OnInit, AfterViewInit {

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  dataSource: MatTableDataSource<ActivityLog> = new MatTableDataSource();

    /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
    displayedColumns = ['id', 'user', 'action', 'time', 'status', 'actions'];

  constructor(
    private activityLogApiService: ActivityLogApiService,
    private dialogService: DialogService,
    private _snackBar: MatSnackBar
  ) { }

  ngOnInit() {
    this.refreshActivityLogs();
  }

  private refreshActivityLogs() {
    this.activityLogApiService.getPageableActivityLog().subscribe(activityLogs => {
      this.dataSource.data = activityLogs.content;
    });
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  applyFilter(filterValue: string): void {
    filterValue = filterValue.trim().toLocaleLowerCase();
    this.dataSource.filter = filterValue;
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
              this.refreshActivityLogs();
            });
        }
      }
    );
  }

}
