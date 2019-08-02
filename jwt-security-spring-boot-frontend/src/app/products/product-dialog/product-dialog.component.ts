import { Component, OnInit, Inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { ProductState } from '../models/product';

@Component({
  selector: 'app-product-dialog',
  templateUrl: './product-dialog.component.html',
  styleUrls: ['./product-dialog.component.scss']
})
export class ProductDialogComponent implements OnInit {

  form: FormGroup;

  productStates: string[];

  constructor(
      private fb: FormBuilder,
      private dialogRef: MatDialogRef<ProductDialogComponent>,
      @Inject(MAT_DIALOG_DATA) data) {

      this.productStates = this.enumSelector(ProductState);

      this.form = this.fb.group({
        name: [data.name, [Validators.required]],
        description: [data.description, [Validators.required]],
        price: [data.price, [Validators.required, Validators.min(0)]],
        state: [data.state, [Validators.required]],
      });

  }

  ngOnInit() {
  }

  enumSelector(someEnum): string[] {
    return Object.keys(someEnum).filter(f => !isNaN(Number(f)))
              // .map(key => ({ id: someEnum[key], value: key }));
              .map(key =>  someEnum[key] );
 }

  save() {
      this.dialogRef.close(this.form.value);
  }

}
