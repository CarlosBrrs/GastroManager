import {Inject, Injectable} from '@angular/core';
import {AuthRepository} from "../../domain/ports/auth.repository";
import {Observable} from "rxjs";
import {LoginResponse} from "../../domain/models/login-response.model";
import {Login} from "../../domain/models/login.model";
import {LoginAdapter} from "../../infrastructure/api/login.adapter";

@Injectable({
  providedIn: 'root'
})
export class LoginUseCase {

  constructor(@Inject(LoginAdapter) private readonly authRepo: AuthRepository) {
  }

  execute(credentials: Login): Observable<LoginResponse> {
    return this.authRepo.login(credentials);
  }
}
