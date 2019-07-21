import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { MatPaginator, MatSort, MatTableDataSource, MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material';
import { ProductService } from '../services/product.service';
import { Product } from './models/product';
import { ConfirmationDialogComponent } from '../shared/confirmation-dialog/confirmation-dialog.component';
import { ProductDialogComponent } from './product-dialog/product-dialog.component';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss']
})
export class ProductsComponent implements OnInit, AfterViewInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  dataSource: MatTableDataSource<Product> = new MatTableDataSource();

  checked = false;

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['id', 'name', 'description', 'price', 'state', 'actions'];

  constructor(
    private dialog: MatDialog,
    private productApiService: ProductService
    ) { }

  ngOnInit() {
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
        if (data !== undefined) {
          this.productApiService.addProduct(data).subscribe();
        }
      }
    );
  }

  editProduct(product: Product) {
    this.openProductDialog(product).afterClosed().subscribe(
      data => {
        if (data !== undefined) {
          this.productApiService.updateProduct(product.id, data)
        .subscribe(response => console.log(response));
        }
      }
    );
  }

  deleteProduct(product: Product) {
    this.openConfirmDialog().afterClosed().subscribe(
      data => {
        if (data !== undefined) {
          this.productApiService.deleteProduct(product.id)
        .subscribe(response => console.log(response));
        }
      }
    );
  }

}

