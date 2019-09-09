import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogConfig, MatDialogRef, MatPaginator, MatSnackBar, MatSort } from '@angular/material';
import { fromEvent, merge } from 'rxjs';
import { debounceTime, distinctUntilChanged, tap } from 'rxjs/operators';
import { PrivilegeResponseDto, RoleResponseDto } from 'src/app/app-security-module/models/role';
import { RoleApiService } from 'src/app/app-security-module/services/role-api.service';
import { DialogService } from 'src/app/app-security-module/shared/common-dialogs/dialog.service';
import { GenericDataSource } from '../../models/generic.datasource';
import { RoleDialogComponent } from './role-dialog/role-dialog.component';

@Component({
  selector: 'app-roles',
  templateUrl: './roles.component.html',
  styleUrls: ['./roles.component.scss']
})
export class RolesComponent implements OnInit, AfterViewInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;

  @ViewChild(MatSort) sort: MatSort;

  @ViewChild('input') input: ElementRef;

  dataSource: GenericDataSource<RoleResponseDto>;

  pageSize = 10;
  pageSizeOptions = [10, 50, 100];

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['id', 'name', 'description', 'privilegeNames', 'actions'];

  constructor(
    private dialog: MatDialog,
    private roleApiService: RoleApiService,
    private dialogService: DialogService,
    private _snackBar: MatSnackBar
  ) { }

  ngOnInit() {
    this.dataSource = new GenericDataSource(this.roleApiService);
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

  getPrivilegeNames(privileges: PrivilegeResponseDto[]): string[] {
    return privileges.map(privilege => privilege.name);
  }

  openRoleDialog(roleData: RoleResponseDto): MatDialogRef<RoleDialogComponent> {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;

    dialogConfig.data = roleData;

    return this.dialog.open(RoleDialogComponent, dialogConfig);
  }

  createRole() {
    this.openRoleDialog(new RoleResponseDto()).afterClosed().subscribe(
      data => {
        if (data) {
          this.roleApiService.create(data).subscribe(
            response => {
              this._snackBar.open('Role has been created successfully');
              this.loadContent();
            }
          );
        }
      }
    );
  }

  editRole(roleId: number) {
    this.roleApiService.getRole(roleId).subscribe(role => {
      this.openRoleDialog(role).afterClosed().subscribe(
        data => {
          if (data) {
            this.roleApiService.update(role.id, data).subscribe(
              response => {
                this._snackBar.open('Role has been updated successfully');
                this.loadContent();
              });
          }
        }
      );
    });
  }

  deleteRole(roleId: number) {
    this.dialogService.openConfirmDialog({
      message: 'Are you sure to delete this role?',
      okName: 'Delete',
      title: 'Deletion'
    }).afterClosed().subscribe(
      data => {
        if (data) {
          this.roleApiService.deleteRole(roleId).subscribe(
            response => {
              this._snackBar.open('Role has been deleted successfully');
              this.loadContent();
            }
          );
        }
      }
    );
  }

}
