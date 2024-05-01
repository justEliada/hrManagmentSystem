import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserResponseDto } from 'src/app/core/models/userResponse.model';
import { AuthServiceService } from 'src/app/core/services/auth-service.service';


@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrl = 'http://localhost:8080/auth/user'; 

  constructor(private http: HttpClient, private AuthService: AuthServiceService) { }

  getAllUsers(): Observable<UserResponseDto[]> {
    return this.http.get<UserResponseDto[]>(`${this.baseUrl}/`);
  }

  getUserById(id: number): Observable<UserResponseDto> {
    return this.http.get<UserResponseDto>(`${this.baseUrl}/${id}`);
  }

  deleteUser(id: number): Observable<string> {
    return this.http.delete(`${this.baseUrl}/delete/${id}`, { responseType: 'text' });
  }
  
  editUser(updatedUser: UserResponseDto): Observable<UserResponseDto> {
    return this.http.post<UserResponseDto>(`${this.baseUrl}/edit/${updatedUser.id}`, updatedUser);
  }  
  
  getUsersByLeastCreatedTimeSheet(): Observable<UserResponseDto[]> {
    return this.http.get<UserResponseDto[]>(`${this.baseUrl}/filter`);
  }
  
  getUserDaysOffById(id: number): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/days-off/${id}`);
  }

  getCurrentUserDaysOff(): Observable<number> {
    const user = this.AuthService.getCurrentUser();
    const userId = user?.id;
    return this.http.get<number>(`${this.baseUrl}/days-off/${userId}`);
  }

}