import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthServiceService } from 'src/app/core/services/auth-service.service';
import { ToastService } from 'src/app/core/services/toast.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent implements OnInit {
  signupForm!: FormGroup;

  constructor(
    private formBuilder: FormBuilder, 
    private router: Router, 
    private authService: AuthServiceService,
    private toastService:ToastService
    ) {}

  ngOnInit(): void {
    this.signupForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }


  onSubmit() {
    if (this.signupForm.valid) {
      this.authService.signup(this.signupForm.value).subscribe({
        next: (response) => {
          console.log('Signup successful', response);
          this.router.navigate(['']);
        },
        error: (error) => {
          this.toastService.show(
            `${error || 'Error submitting signup request'}`,
            'error'
          );
        },
      });
    } else {
      console.log('Form is not valid');
      this.signupForm.markAllAsTouched();
    }
  }
}