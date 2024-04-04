import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';



@Component({
  selector: 'app-side-bar',
  templateUrl: './side-bar.component.html',
  styleUrls: ['./side-bar.component.scss']
})

export class SideBarComponent implements OnInit {
  userName: string = 'Hello';
  userRole: string = 'Default Role';
  
  constructor(

     private router: Router) { }

    ngOnInit(): void {
        throw new Error('Method not implemented.');
    }

  navigateToDashboard() {
    const userRole = this.userRole.toUpperCase(); 
    switch(userRole) {
      case 'MANAGER':
        this.router.navigate(['/dashboard']);
        break;
      case 'USER':
        console.log('shsh');
        this.router.navigate(['/user-dashboard']);
        break;
      default:
        console.error('Unexpected role:', userRole);
        break;
    }
  }

  navigateToVacationList(){
    const userRole = this.userRole.toUpperCase(); 
    switch(userRole) {
      case 'MANAGER':
        this.router.navigate(['/dashboard']);
        break;
      case 'USER':
        this.router.navigate(['/user-dashboard/vacations-list']);
        break;
      default:
        console.error('Unexpected role:', userRole);
        break;
    }
  }

  logout() {
    console.log('logged out');
    this.router.navigate(['']); 
  }
}