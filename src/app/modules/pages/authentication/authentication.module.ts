import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router'; 
import { ReactiveFormsModule, FormsModule} from '@angular/forms';
import { SharedModule } from 'src/app/shared.module';
import { LoginComponent } from './login/login/login.component';
import { SignupComponent } from './signup/signup/signup.component';


const routes: Routes = [
    {
      path: '',
      component: LoginComponent
    },
    {
      path: 'signup',
      component: SignupComponent
    },
];

@NgModule({
  declarations: [
    LoginComponent,
    SignupComponent,
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    SharedModule,
    RouterModule.forChild(routes),
  ],
  exports: [RouterModule] 
  
})
export class AuthenticationModule { }