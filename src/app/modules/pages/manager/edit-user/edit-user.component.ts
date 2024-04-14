import { Component, OnInit, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { UserService } from 'src/app/modules/services/user.service';
import { ActivatedRoute, Router } from '@angular/router';
import { UserResponseDto } from 'src/app/core/models/userResponse.model';
import { VacationRequest } from 'src/app/core/models/vacationRequest.model';
import { ToastService } from 'src/app/core/services/toast.service';
import { VacationRequestService } from 'src/app/modules/services/vacation-request.service';
import { VacationResponse } from 'src/app/core/models/VacationResponse.model';
import { CalendarModalComponent } from 'src/app/modules/components/calendar-modal/calendar-modal/calendar-modal.component';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.scss'],
})

export class EditUserComponent implements OnInit {
  editForm: FormGroup;
  selectedEmployee: UserResponseDto | undefined;
  userRequestNotes: VacationResponse[] = [];
  showModal: boolean = false;
  selectedUserDaysOff: number | undefined;
  showCalendar: boolean = false;

  vacationRequest: VacationRequest = {
    fromDate: new Date(),
    toDate: new Date(),
    notes: '',
    createdBy: '',
    userId: 0,
  };

  constructor(
    private formBuilder: FormBuilder,
    private userService: UserService,
    private vacationRequestService: VacationRequestService,
    private route: ActivatedRoute,
    private router: Router,
    private toastService: ToastService,
  ) {
    this.editForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      username: ['', Validators.required],
      daysOff: ['']
    });
  }

 
  ngOnInit() {
    this.getUser();
    this.getUserVacationRequest();
  }
  
  handleShowChange(show: boolean) {
    this.showCalendar = show;
  }

  openModal() {
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
  }

  getUser() {
    const userId = +this.route.snapshot.params['id'];
    if (userId) {
      this.userService.getUserById(userId).subscribe({
        next: (data) => {
          this.selectedEmployee = data;
          this.editForm.patchValue(this.selectedEmployee);
          this.getSelectedUserDaysOff(userId);
        },
        error: (e) => console.error(e),
      });
    }
  }


  getSelectedUserDaysOff(userId: number) {
    this.userService.getUserDaysOffById(userId).subscribe({
      next: (daysOff) => {
        this.selectedUserDaysOff = daysOff;
      },
      error: (e) => console.error(e),
    });
  }

  updateSelectedUserDaysOff() {
    const userId = this.selectedEmployee?.id;
    if (userId) {
      this.userService.getUserDaysOffById(userId).subscribe({
        next: (daysOff) => {
          this.selectedUserDaysOff = daysOff;
        },
        error: (e) => console.error(e),
      });
    }
  }
  

          
  saveChanges() {
    if (this.editForm.valid) {
      const updatedUser: UserResponseDto = {
        ...this.selectedEmployee,
        ...this.editForm.value,
      };
      this.userService.editUser(updatedUser).subscribe({
        next: (response) => {
          this.toastService.show(
            'User Data Saved Successfully'
          );
          this.router.navigate(['/dashboard']);
        },
        error: (e) => {
          console.error(e);
          console.log('Error updating user');
        },
      });
    } else {
      console.log('Form is invalid');
    }
  }

  getUserVacationRequest() {
    const userId = this.route.snapshot.params['id'];
    if (userId) {
      this.vacationRequestService.getAllVacationRequestsById(userId).subscribe({
        next: (response) => {
          this.userRequestNotes = response;
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

  approveUserRequest(id: number) {
    if (id) {
      this.vacationRequestService.approveUserRequest(id).subscribe({
        next: (response) => {
          this.toastService.show(response.message, 'success');
          this.getUserVacationRequest();
          this.updateSelectedUserDaysOff(); 
        },
        error: (error) => {
          this.toastService.show(
            `${error.error || 'Error approving vacations!'}`,
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

  rejectUserRequest(id: number) {
    if (id) {
      this.vacationRequestService.rejectUserRequest(id).subscribe({
        next: (response) => {
          this.toastService.show(response.message, 'success');
          this.getUserVacationRequest();
          this.updateSelectedUserDaysOff(); 
        },
        error: (error) => {
          this.toastService.show(
            `${error.error || 'Error rejecting vacations!'}`,
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

  submitVacationRequest() {
    const userId = this.route.snapshot.params['id'];
    if (userId) {
      this.vacationRequest.userId = userId;
      this.vacationRequestService
        .submitVacationRequestManager(this.vacationRequest)
        .subscribe({
          next: (response) => {
            this.toastService.show(
              'Vacation request submitted successfully',
              'success'
            );
            this.getUserVacationRequest();
            this.vacationRequest = {
              fromDate: new Date(),
              toDate: new Date(),
              notes: '',
              createdBy: '',
              userId: 0,
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
}