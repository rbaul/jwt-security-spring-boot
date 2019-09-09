import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogConfig, MatDialogRef, MatPaginator, MatSnackBar, MatSort } from '@angular/material';
import { fromEvent, merge } from 'rxjs';
import { debounceTime, distinctUntilChanged, tap } from 'rxjs/operators';
import { GenericDataSource } from '../app-security-module/models/generic.datasource';
import { DialogService } from '../app-security-module/shared/common-dialogs/dialog.service';
import { Product } from './models/product';
import { ProductDialogComponent } from './product-dialog/product-dialog.component';
import { ProductService } from './services/product.service';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss']
})
export class ProductsComponent implements OnInit, AfterViewInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;

  @ViewChild(MatSort) sort: MatSort;

  @ViewChild('input') input: ElementRef;

  dataSource: GenericDataSource<Product>;

  pageSize = 10;
  pageSizeOptions = [10, 50, 100];

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['id', 'name', 'description', 'price', 'state', 'actions'];

  constructor(
    private dialog: MatDialog,
    private productApiService: ProductService,
    private dialogService: DialogService,
    private _snackBar: MatSnackBar
  ) { }

  ngOnInit() {
    this.dataSource = new GenericDataSource(this.productApiService);
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
              this.loadContent();
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
              this.loadContent();
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
              this.loadContent();
            });
        }
      }
    );
  }

}

