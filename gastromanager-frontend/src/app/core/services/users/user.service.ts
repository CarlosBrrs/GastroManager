import {Injectable, signal} from '@angular/core';
import {UserResponseDto} from "../../model/interfaces/UserResponseDto";
import {Observable, of, tap} from "rxjs";
import {ApiGenericResponse} from "../../model/interfaces/ApiGenericResponse";
import {AuthService} from "../auth/auth.service";
import {BaseHttpService} from "../basehttp/base-http.service";

@Injectable({
  providedIn: 'root'
})
export class UserService extends BaseHttpService {

  userInfo = signal<UserResponseDto | undefined>(undefined);

  constructor(private authService: AuthService) {
    super();
  }

  loadUserInfo(): Observable<ApiGenericResponse<UserResponseDto>> {
    return this.http.get<ApiGenericResponse<UserResponseDto>>("http://localhost:8080/api/v1/users/" + this.authService.getUserUuid(), {
      headers: {'Accept': 'application/json'}
    }).pipe(
      tap(response => {
        if (response.flag) {
          console.log("User info from API")
          this.userInfo.set(response.data)

        }
      })
    );
  }

  getRole(): string {
    return this.userInfo()?.roles[0].name || "";
  }
}
