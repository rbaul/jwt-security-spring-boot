import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { AdministrationComponent } from './administration/administration.component';
import { AuthGuard } from './security/auth.guard';


const routes: Routes = [
  { path: 'login', component: LoginComponent},
  { path: 'administration', component: AdministrationComponent, canActivate: [AuthGuard], data: {privileges: ['ROLE_ADMIN_PRIVILEGE']} },
  { path: '**', redirectTo: 'login'}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AppSecurityRoutingModule { }
