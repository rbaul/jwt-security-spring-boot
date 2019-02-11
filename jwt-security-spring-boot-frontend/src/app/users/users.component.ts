import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { MatPaginator, MatSort, MatTableDataSource, MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material';
import { ConfirmationDialogComponent } from '../shared/confirmation-dialog/confirmation-dialog.component';
import { UserApiService } from '../services/user-api.service';
import { User } from '../security/models/user';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent implements OnInit, AfterViewInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  dataSource: MatTableDataSource<User> = new MatTableDataSource();

  checked = false;

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['id', 'username', 'lastname', 'firstname', 'actions'];

  constructor(
    private dialog: MatDialog,
    private userApiService: UserApiService
    ) { }

  ngOnInit() {
    this.userApiService.getAllUsers().subscribe(users => {
      this.dataSource.data = users;
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

  openConfirmDialog(): MatDialogRef<ConfirmationDialogComponent> {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;

    dialogConfig.data = {
      message: 'Are you sure to delete this product?',
      okName: 'Delete',
      title: 'Deletion'
    };

    return this.dialog.open(ConfirmationDialogComponent, dialogConfig);
  }

  // openProductDialog(productData: Product): MatDialogRef<ProductDialogComponent> {
  //   const dialogConfig = new MatDialogConfig();

  //   dialogConfig.disableClose = true;
  //   dialogConfig.autoFocus = true;

  //   dialogConfig.data = productData;

  //   return this.dialog.open(ProductDialogComponent, dialogConfig);
  // }

  createUser() {
    console.log('Delete user not implemented');
    // this.openProductDialog(new User()).afterClosed().subscribe(
    //   data => {
    //     if (data !== undefined) {
    //       this.userApiService.singup(data).subscribe();
    //     }
    //   }
    // );
  }

  editUser(user: User) {
    console.log('Edit user not implemented');
    // this.openProductDialog(user).afterClosed().subscribe(
    //   data => {
    //     if (data !== undefined) {
    //       this.userApiService.updateUser(user.id, data)
    //     .subscribe(response => console.log(response));
    //     }
    //   }
    // );
  }

  deleteUser(user: User) {
    this.openConfirmDialog().afterClosed().subscribe(
      data => {
        if (data !== undefined) {
          console.log('Delete user not implemented');
        //   this.userApiService.deleteProduct(product.id)
        // .subscribe(response => console.log(response));
        }
      }
    );
  }

}


