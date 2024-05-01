import { Component, OnInit } from '@angular/core';
import { VacationRequest } from 'src/app/core/models/vacationRequest.model';
import { AuthServiceService } from 'src/app/core/services/auth-service.service';
import { VacationRequestService } from 'src/app/modules/services/vacation-request.service';
import { ToastService } from 'src/app/core/services/toast.service';
import { VacationResponse } from 'src/app/core/models/VacationResponse.model';
import { UserService } from 'src/app/modules/services/user.service';

@Component({
  selector: 'app-user-dashboard',
  templateUrl: './user-dashboard.component.html',
  styleUrls: ['./user-dashboard.component.scss'],
})
export class UserDashboardComponent implements OnInit {
  employee: any;
  daysOff: number = 0;

  vacationRequest: VacationRequest = {
    fromDate: new Date(),
    toDate: new Date(),
    notes: '',
    createdBy: '',
    userId: 0,
  };

  vacationList: VacationResponse[] = [];

  constructor(
    private authService: AuthServiceService,
    private vacationRequestService: VacationRequestService,
    private toastService: ToastService,
    private userService: UserService
  ) { }

  ngOnInit() {
    this.loadCurrentUserAndDaysOff();
    this.getVacationRequests();
  }

  loadUserDaysOff() {
    this.userService.getCurrentUserDaysOff().subscribe({
      next: (days: number) => {
        this.daysOff = days;
      },
      error: (error) => {
        console.error('Error fetching days off', error);
      },
    });
  }

  loadCurrentUserAndDaysOff() {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser) {
      this.employee = currentUser;
      this.vacationRequest.createdBy = currentUser.username;
      this.vacationRequest.userId = currentUser.id;
      this.loadUserDaysOff();
    } else {
      console.error('No current user found');
    }
  }

  submitVacationRequest() {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser && currentUser.username) {
      this.vacationRequest.createdBy = currentUser.username;
      this.vacationRequest.userId = currentUser.id;
      this.vacationRequestService
        .submitVacationRequest(this.vacationRequest)
        .subscribe({
          next: (response) => {
            this.toastService.show(
              'Vacation request submitted successfully',
              'success'
            );
            this.loadCurrentUserAndDaysOff();
            this.getVacationRequests();
            this.vacationRequest = {
              fromDate: new Date(),
              toDate: new Date(),
              notes: '',
              createdBy: currentUser.username,
              userId: currentUser.id,
            };
          },
          error: (error) => {
            this.toastService.show(
              `${error.error || 'Error submitting vacation request'}`,
              'error'
            );
          },
        });
    } else {
      this.toastService.show(
        'No user data found for the vacation request',
        'error'
      );
    }
  }
  getVacationRequests() {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser && currentUser.username) {
      this.vacationRequestService
        .getVacationRequests(currentUser.id)
        .subscribe({
          next: (response) => {
            this.vacationList = response
          },
          error: (error) => {
            this.toastService.show(
              `${error.error || 'Error getting vacations!'}`,
              'error'
            );
          },
        });
    } else {
      this.toastService.show(
        'No user data found for the vacation request',
        'error'
      );
    }
  }
}
