import {Injectable, signal} from '@angular/core';
import {UserResponseDto} from "../../model/interfaces/UserResponseDto";
import {Observable, tap} from "rxjs";
import {ApiGenericResponse} from "../../model/interfaces/ApiGenericResponse";
import {AuthService} from "../auth/auth.service";
import {BaseHttpService} from "../basehttp/base-http.service";

@Injectable({
  providedIn: 'root'
})
export class UserService extends BaseHttpService {

  userInfo = signal<UserResponseDto | undefined>(undefined);
  private userInfoKey: string = 'userInfo';

  constructor(private authService: AuthService) {
    super();
    this.initUserInfo();
  }

  loadUserInfo(): Observable<ApiGenericResponse<UserResponseDto>> {
    return this.http.get<ApiGenericResponse<UserResponseDto>>("http://localhost:8080/api/v1/users/" + this.authService.getUserUuid(), {
      headers: {'Accept': 'application/json'}
    }).pipe(
      tap(response => {
        console.log("in the tap")
        if (response.flag) {
          console.log("User info from API")
          const data = response.data;
          const {uuid, roles, ...basicUserInfo} = data;
          this.userInfo.set(data)
          localStorage.setItem(this.userInfoKey, JSON.stringify(basicUserInfo));
          console.log(basicUserInfo)
        }
      })
    );
  }

  private initUserInfo() {
    const storedUserInfo = localStorage.getItem(this.userInfoKey);
    if (storedUserInfo) {
      this.userInfo.set(JSON.parse(storedUserInfo));
    } else {
      this.loadUserInfo().subscribe(); // cargar desde la API si no hay datos en localStorage
    }
  }
}
