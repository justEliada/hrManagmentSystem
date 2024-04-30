import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthServiceService } from 'src/app/core/services/auth-service.service';
import { ToastService } from 'src/app/core/services/toast.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup; 

  constructor(
    private formBuilder: FormBuilder, 
    private router: Router, 
    private authService: AuthServiceService,
    private toastService: ToastService) { }


  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required]], 
      password: ['', Validators.required],
    });
  }


  onSubmit() {
    if (this.loginForm.valid) {
      this.authService.login(this.loginForm.value.username, this.loginForm.value.password)
        .subscribe({
          next: (response: any) => {
            console.log('Login successful', response);
            if (response && response.role) {
              this.authService.setAuthentication(true);
              switch(response.role.toUpperCase()) {
                case 'MANAGER':
                  this.router.navigate(['/dashboard']); 
                  break;
                case 'USER':
                  this.router.navigate(['/user-dashboard']); 
                  break;
                default:
                  console.error('Unexpected role or unauthenticated user');
                  break;
              }
            } else {
              console.error('Login failed: No user returned');
              this.authService.setAuthentication(false);
            }
          },
          error: (error: any) => {
            this.toastService.show(
              `${error || 'Error submitting signup request'}`,
              'error'
            );
            this.authService.setAuthentication(false);
          }
        });
    } else {
      console.log('Form is not valid');
      this.loginForm.markAllAsTouched();
    }
  }
  
  
}
