import { Injectable } from '@angular/core';
import {LoginUseCase} from "../usecases/login.use-case";
import {Observable} from "rxjs";
import {Login} from "../../domain/models/login.model";
import {LoginResponse} from "../../domain/models/login-response.model";

@Injectable({
  providedIn: 'root'
})
export class AuthFacade {

  constructor(private readonly loginUC: LoginUseCase) { }

  login(credentials: Login): Observable<LoginResponse> {
    return this.loginUC.execute(credentials);
  }
}
