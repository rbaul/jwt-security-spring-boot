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

  name: string;
  description: string;
  price: number;
  state: ProductState;

  productStates;

  constructor(
      private fb: FormBuilder,
      private dialogRef: MatDialogRef<ProductDialogComponent>,
      @Inject(MAT_DIALOG_DATA) data) {

      this.name = data.name;
      this.description = data.description;
      this.price = data.price;
      this.state = data.state;

  }

  ngOnInit() {
    this.productStates = this.enumSelector(ProductState);

    this.form = this.fb.group({
      name: [this.name, [Validators.required]],
      description: [this.description, [Validators.required]],
      price: [this.price, [Validators.required, Validators.min(0)]],
      state: [this.state, [Validators.required]],
    });
  }

  enumSelector(someEnum) {
    return Object.keys(someEnum).filter(f => !isNaN(Number(f)))
              // .map(key => ({ id: someEnum[key], value: key }));
              .map(key =>  someEnum[key] );
 }

  save() {
      this.dialogRef.close(this.form.value);
  }

  close() {
      this.dialogRef.close();
  }

}
