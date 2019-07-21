import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { ProductsComponent } from './products/products.component';
import { AuthGuard } from './security/auth.guard';
import { UsersComponent } from './users/users.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent},
  { path: 'users', component: UsersComponent, canActivate: [AuthGuard], data: {privileges: ['ROLE_ADMIN_PRIVILEGE']} },
  { path: 'products', component: ProductsComponent, canActivate: [AuthGuard],
   data: {privileges: ['ROLE_VIEW_PRIVILEGE', 'ROLE_CHANGE_PRIVILEGE']} },
  { path: '', redirectTo: 'products', pathMatch: 'full'},
  { path: '**', redirectTo: 'login'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
