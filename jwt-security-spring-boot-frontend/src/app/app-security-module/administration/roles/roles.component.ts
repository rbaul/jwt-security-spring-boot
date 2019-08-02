import { Component, OnInit, AfterViewInit, ViewChild } from '@angular/core';
import { MatDialog, MatSort, MatPaginator, MatTableDataSource, MatDialogRef, MatDialogConfig } from '@angular/material';
import { RoleApiService } from 'src/app/app-security-module/services/role-api.service';
import { RoleResponseDto, PrivilegeResponseDto } from 'src/app/app-security-module/models/role';
import { DialogService } from 'src/app/app-security-module/shared/common-dialogs/dialog.service';
import { RoleDialogComponent } from './role-dialog/role-dialog.component';

@Component({
  selector: 'app-roles',
  templateUrl: './roles.component.html',
  styleUrls: ['./roles.component.scss']
})
export class RolesComponent implements OnInit, AfterViewInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  dataSource: MatTableDataSource<RoleResponseDto> = new MatTableDataSource();

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['id', 'name', 'description', 'privilegeNames', 'actions'];

  constructor(
    private dialog: MatDialog,
    private roleApiService: RoleApiService,
    private dialogService: DialogService
    ) { }

  ngOnInit() {
    this.refreshRoles();
  }

  private refreshRoles() {
    this.roleApiService.getAllRoles().subscribe(roles => {
      this.dataSource.data = roles;
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
            response => this.refreshRoles()
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
              response => this.refreshRoles()
            );
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
              this.refreshRoles();
            }
          );
        }
      }
    );
  }

}
