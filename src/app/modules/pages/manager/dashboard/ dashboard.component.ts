import { Component, Input, OnInit, SimpleChanges } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ExtendedUserResponseDto } from 'src/app/core/models/ExtendedUserResponseDto.model';
import { UserService } from 'src/app/modules/services/user.service';
import { VacationRequestService } from 'src/app/modules/services/vacation-request.service';


@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']

})
export class DashboardComponent implements OnInit {
  isFilterApplied = false;
  users: ExtendedUserResponseDto[] = [];
  allUsers: ExtendedUserResponseDto[] = [];

  constructor(
    private userService: UserService,
    private router: Router,
    private route: ActivatedRoute,
    private vacationRequestService: VacationRequestService, ) { }

  ngOnInit(): void {
    this.loadAllUsers();
  }

  ngOnChanges(changes: SimpleChanges): void {
    console.log('DashboardComponent: ngOnChanges', changes);
    if (changes['searchText'] && !changes['searchText'].firstChange) {
      this.filterUsers(changes['searchText'].currentValue);
    }
  }

  loadAllUsers(): void {
  }

  handleSearch(searchText: string): void {
    this.filterUsers(searchText);
  }

  editUser(userId: number): void {
    console.log(`Navigating to edit user with ID: ${userId}`);
    this.router.navigate(['/dashboard/edit-user', userId]).catch(err => console.error('Navigation error:', err));
  }
  
  filterUsers(text: string): void {
    if (!text) {
      this.users = [...this.allUsers];
      return;
    }
    this.users = this.allUsers.filter(user =>
      user.firstName.toLowerCase().includes(text.toLowerCase()) ||
      user.lastName.toLowerCase().includes(text.toLowerCase())
    );
  }

  toggleFilter(): void {
  }

  checkForPendingVacations(usersToCheck: ExtendedUserResponseDto[] = this.users): void {
  }
  
  deleteUser(id: number): void {
    if (confirm('Are you sure you want to delete this user?')) {
      this.userService.deleteUser(id).subscribe({
        next: (response: any) => {
          console.log(response);
          this.users = this.users.filter(user => user.id !== id);
        },
        error: (e: any) => {
          console.error('Error deleting user:', e);
          alert('Failed to delete user. Please try again.');
        }
      });
    }
  }
}