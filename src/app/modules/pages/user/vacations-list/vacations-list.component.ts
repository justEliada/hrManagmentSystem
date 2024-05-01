import { Component, OnInit } from '@angular/core';
import { AuthServiceService } from 'src/app/core/services/auth-service.service';
import { VacationRequestService } from 'src/app/modules/services/vacation-request.service';

@Component({
  selector: 'app-vacations-list',
  templateUrl: './vacations-list.component.html',
  styleUrls: ['./vacations-list.component.scss']
})
export class VacationsListComponent implements OnInit {
  originalVacationList: any[] = [];
  vacationList: any[] = [];

  constructor(
    private vacationRequestService: VacationRequestService,
    private authService: AuthServiceService
  ) { }

  ngOnInit(): void {
    this.loadVacationRequestsForCurrentUser();
  }

  loadVacationRequestsForCurrentUser() {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser && currentUser.id) {
      this.vacationRequestService.getAllVacationRequestsById(currentUser.id).subscribe({
        next: (data) => {
          this.originalVacationList = data;
          this.vacationList = data;
        },
        error: (err) => console.error(err),
      });
    } else {
      console.error('No current user found');
    }
  }

  filterVacations(status: string) {
    if (status === 'ALL') {
      this.vacationList = this.originalVacationList;
    } else {
      this.vacationList = this.originalVacationList.filter(vacation => vacation.status === status);
    }
  }
}

