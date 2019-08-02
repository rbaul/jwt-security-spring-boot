import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { LoginComponent } from './login/login.component';
import { NavbarComponent } from './navbar/navbar.component';
import { UsersComponent } from './administration/users/users.component';
import { ConfirmationDialogComponent } from './shared/common-dialogs/confirmation-dialog/confirmation-dialog.component';
import { HasPrivilegeDirective } from './security/has-privilege.directive';
import { UserDialogComponent } from './administration/users/user-dialog/user-dialog.component';
import { AlertDialogComponent } from './shared/common-dialogs/alert-dialog/alert-dialog.component';
import { RolesComponent } from './administration/roles/roles.component';
import { AdministrationComponent } from './administration/administration.component';
import { RoleDialogComponent } from './administration/roles/role-dialog/role-dialog.component';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from './shared/material.module';
import { FlexLayoutModule } from '@angular/flex-layout';
import { HttpInterceptorModule } from './security/http-interceptor.module';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from '../app-routing.module';
import { AppSecurityRoutingModule } from './app-security-routing.module';
import { LoginDialogComponent } from './login/login-dialog/login-dialog.component';


@NgModule({
  declarations: [
    LoginComponent,
    NavbarComponent,
    UsersComponent,
    ConfirmationDialogComponent,
    HasPrivilegeDirective,
    UserDialogComponent,
    AlertDialogComponent,
    RolesComponent,
    AdministrationComponent,
    RoleDialogComponent,
    LoginDialogComponent
  ],
  imports: [
    CommonModule,
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    FlexLayoutModule,
    HttpInterceptorModule,
    MaterialModule,
    AppSecurityRoutingModule
  ],
  exports: [
    LoginComponent,
    NavbarComponent,
    UsersComponent,
    ConfirmationDialogComponent,
    HasPrivilegeDirective,
    UserDialogComponent,
    AlertDialogComponent,
    RolesComponent,
    AdministrationComponent,
    RoleDialogComponent
  ],
  providers: [],
  entryComponents: [
    ConfirmationDialogComponent,
    UserDialogComponent,
    AlertDialogComponent,
    RoleDialogComponent,
    LoginDialogComponent
  ]
})
export class AppSecurityModule { }
