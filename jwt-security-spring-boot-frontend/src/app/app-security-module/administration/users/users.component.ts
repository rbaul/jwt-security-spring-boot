import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { MatPaginator, MatSort, MatTableDataSource, MatDialog, MatDialogConfig, MatDialogRef, MatSnackBar } from '@angular/material';
import { UserApiService } from '../../services/user-api.service';
import { UserResponseDto } from '../../models/user';
import { UserDialogComponent } from './user-dialog/user-dialog.component';
import { DialogService } from '../../shared/common-dialogs/dialog.service';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent implements OnInit, AfterViewInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  dataSource: MatTableDataSource<UserResponseDto> = new MatTableDataSource();

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['id', 'username', 'lastname', 'firstname', 'roleNames', 'actions'];

  constructor(
    private dialog: MatDialog,
    private userApiService: UserApiService,
    private dialogService: DialogService,
    private _snackBar: MatSnackBar
    ) { }

  ngOnInit() {
    this.refreshUsers();
  }

  private refreshUsers() {
    this.userApiService.getPageableUsers().subscribe(users => {
      this.dataSource.data = users.content;
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
              this.refreshUsers();
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
                this.refreshUsers();
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
              this.refreshUsers();
            }
          );
        }
      }
    );
  }

}


