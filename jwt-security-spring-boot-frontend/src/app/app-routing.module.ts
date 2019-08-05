import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ProductsComponent } from './products/products.component';
import { AuthGuard } from './app-security-module/security/auth.guard';
import { ActivityLogComponent } from './activity-log/activity-log.component';

const routes: Routes = [
  { path: 'products', component: ProductsComponent, canActivate: [AuthGuard],
   data: {privileges: ['ROLE_VIEW_PRIVILEGE', 'ROLE_CHANGE_PRIVILEGE']} },
  { path: 'activity-logs', component: ActivityLogComponent, canActivate: [AuthGuard],
   data: {privileges: ['ROLE_ACTIVITY_PRIVILEGE']} },
  { path: '', redirectTo: 'products', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
