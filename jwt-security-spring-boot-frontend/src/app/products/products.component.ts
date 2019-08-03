import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { MatPaginator, MatSort, MatTableDataSource, MatDialog, MatDialogConfig, MatDialogRef, MatSnackBar } from '@angular/material';
import { ProductService } from './services/product.service';
import { Product } from './models/product';
import { ProductDialogComponent } from './product-dialog/product-dialog.component';
import { DialogService } from '../app-security-module/shared/common-dialogs/dialog.service';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss']
})
export class ProductsComponent implements OnInit, AfterViewInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  dataSource: MatTableDataSource<Product> = new MatTableDataSource();

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['id', 'name', 'description', 'price', 'state', 'actions'];

  constructor(
    private dialog: MatDialog,
    private productApiService: ProductService,
    private dialogService: DialogService,
    private _snackBar: MatSnackBar
    ) { }

  ngOnInit() {
    this.refreshProducts();
  }

  private refreshProducts() {
    this.productApiService.getPageableProducts().subscribe(products => {
      this.dataSource.data = products.content;
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

  openProductDialog(productData: Product): MatDialogRef<ProductDialogComponent> {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;

    dialogConfig.data = productData;

    return this.dialog.open(ProductDialogComponent, dialogConfig);
  }

  createProduct() {
    this.openProductDialog(new Product()).afterClosed().subscribe(
      data => {
        if (data) {
          this.productApiService.addProduct(data)
          .subscribe(response => {
            this._snackBar.open('Product has been created successfully');
            this.refreshProducts();
          });
        }
      }
    );
  }

  editProduct(product: Product) {
    this.openProductDialog(product).afterClosed().subscribe(
      data => {
        if (data) {
          this.productApiService.updateProduct(product.id, data)
            .subscribe(response => {
              this._snackBar.open('Product has been updated successfully');
              this.refreshProducts();
            });
        }
      }
    );
  }

  deleteProduct(product: Product) {
    this.dialogService.openConfirmDialog({
      message: 'Are you sure to delete this product?',
      okName: 'Delete',
      title: 'Deletion'
    }).afterClosed().subscribe(
      data => {
        if (data) {
          this.productApiService.deleteProduct(product.id)
            .subscribe(response => {
              this._snackBar.open('Product has been deleted successfully');
              this.refreshProducts();
            });
        }
      }
    );
  }

}

