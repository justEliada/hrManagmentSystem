import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NotfoundComponent } from './core/components/page-not-found/notfound/notfound.component';
import { RoleGuard } from './core/guards/role-guard.guard';
import { AuthGuard } from './core/guards/auth-guard.guard';
import { LoginGuard } from './core/guards/login-guard.guard';

const routes: Routes = [

  { 
    path: '', 
    loadChildren: () => import('./modules/pages/authentication/authentication.module').then(m => m.AuthenticationModule), 
    canActivate: [LoginGuard],
  },
  {
    path: 'dashboard',
    loadChildren: () => import('./modules/pages/manager/manager.module').then(m => m.ManagerModule), 
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'MANAGER' }
  },
  {
    path: '**',
    component: NotfoundComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }