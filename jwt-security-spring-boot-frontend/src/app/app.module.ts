import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './shared/material.module';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { LoginComponent } from './login/login.component';
import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';

import { FlexLayoutModule } from '@angular/flex-layout';
import { NavbarComponent } from './navbar/navbar.component';
import { HttpInterceptorModule } from './security/http-interceptor.module';
import { ProductsComponent } from './products/products.component';
import { UsersComponent } from './users/users.component';
import { ConfirmationDialogComponent } from './shared/confirmation-dialog/confirmation-dialog.component';
import { ProductDialogComponent } from './products/product-dialog/product-dialog.component';
import { HasPrivilegeDirective } from './security/has-privilege.directive';
import { ParticleComponent } from './shared/particle/particle.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    NavbarComponent,
    ProductsComponent,
    UsersComponent,
    ConfirmationDialogComponent,
    ProductDialogComponent,
    HasPrivilegeDirective,
    ParticleComponent
  ],
  imports: [
    CommonModule,
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    MaterialModule,
    HttpClientModule,
    FlexLayoutModule,
    HttpInterceptorModule
  ],
  providers: [],
  entryComponents: [ProductDialogComponent, ConfirmationDialogComponent],
  bootstrap: [AppComponent]
})
export class AppModule { }
