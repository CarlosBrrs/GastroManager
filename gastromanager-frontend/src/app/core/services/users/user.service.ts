import {Injectable} from '@angular/core';
import {UserResponseDto} from "../../model/interfaces/UserResponseDto";
import {Observable, tap} from "rxjs";
import {ApiGenericResponse} from "../../model/interfaces/ApiGenericResponse";
import {AuthService} from "../auth/auth.service";
import {BaseHttpService} from "../basehttp/base-http.service";

@Injectable({
  providedIn: 'root'
})
export class UserService extends BaseHttpService {


  constructor(private authService: AuthService) {
    super();
  }

  loadUserInfo(): Observable<ApiGenericResponse<UserResponseDto>> {
    return this.http.get<ApiGenericResponse<UserResponseDto>>("http://localhost:8080/api/v1/users/" + this.authService.getUserUuid(), {
      headers: {'Accept': 'application/json'}
    }).pipe(
      tap(response => {
        if (response.flag) {
          console.log(response.data)
        }
      })
    );
  }
}
