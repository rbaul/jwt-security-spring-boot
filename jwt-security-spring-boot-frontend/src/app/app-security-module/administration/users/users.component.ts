import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogConfig, MatDialogRef, MatPaginator, MatSnackBar, MatSort } from '@angular/material';
import { fromEvent, merge } from 'rxjs';
import { debounceTime, distinctUntilChanged, tap } from 'rxjs/operators';
import { GenericDataSource } from '../../models/generic.datasource';
import { UserResponseDto } from '../../models/user';
import { UserApiService } from '../../services/user-api.service';
import { DialogService } from '../../shared/common-dialogs/dialog.service';
import { UserDialogComponent } from './user-dialog/user-dialog.component';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent implements OnInit, AfterViewInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;

  @ViewChild(MatSort) sort: MatSort;

  @ViewChild('input') input: ElementRef;

  dataSource: GenericDataSource<UserResponseDto>;

  pageSize = 10;
  pageSizeOptions = [10, 50, 100];

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['id', 'username', 'lastName', 'firstName', 'roleNames', 'actions'];

  constructor(
    private dialog: MatDialog,
    private userApiService: UserApiService,
    private dialogService: DialogService,
    private _snackBar: MatSnackBar
  ) { }

  ngOnInit() {
    this.dataSource = new GenericDataSource(this.userApiService);
    this.dataSource.loadContent(this.pageSize, 0, [], '');
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

          this.loadContent();
        })
      )
      .subscribe();

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        tap(() => this.loadContent())
      ).subscribe();
  }

  loadContent() {
    this.dataSource.loadContent(
      this.paginator.pageSize,
      this.paginator.pageIndex,
      this.sort.active && [this.sort.active + ',' + this.sort.direction],
      this.input.nativeElement.value);
  }

  openUserDialog(userData: UserResponseDto): MatDialogRef<UserDialogComponent> {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;

    dialogConfig.data = userData;

    return this.dialog.open(UserDialogComponent, dialogConfig);
  }

  createUser() {
    this.openUserDialog(new UserResponseDto()).afterClosed().subscribe(
      data => {
        if (data) {
          this.userApiService.create(data).subscribe(
            response => {
              this._snackBar.open('User has been created successfully');
              this.loadContent();
            });
        }
      }
    );
  }

  editUser(userId: number) {
    this.userApiService.getUser(userId).subscribe(user => {
      this.openUserDialog(user).afterClosed().subscribe(
        data => {
          if (data) {
            this.userApiService.update(user.id, data).subscribe(
              response => {
                this._snackBar.open('User has been updated successfully');
                this.loadContent();
              });
          }
        }
      );
    });
  }

  deleteUser(userId: number) {
    this.dialogService.openConfirmDialog({
      message: 'Are you sure to delete this user?',
      okName: 'Delete',
      title: 'Deletion'
    }).afterClosed().subscribe(
      data => {
        if (data) {
          this.userApiService.deleteUser(userId).subscribe(
            response => {
              this._snackBar.open('User has been deleted successfully');
              this.loadContent();
            }
          );
        }
      }
    );
  }

}


