import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, Router, RouterStateSnapshot } from '@angular/router';
import { AuthServiceService } from '../services/auth-service.service';


@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {
  constructor(private authService: AuthServiceService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const userRole = this.authService.getCurrentUserRole()?.toUpperCase();
    const roles = route.data['roles'] ? route.data['roles'].map((role: string) => role.toUpperCase()) : [];
  
    if (route.data['role'] && route.data['role'].indexOf(userRole) === -1) {
      if (userRole === 'MANAGER') {
        this.router.navigate(['/dashboard']);
      } else if (userRole === 'USER') {
        this.router.navigate(['/user-dashboard']);
      }
      return false;
    }
    return true;
  }
  
}