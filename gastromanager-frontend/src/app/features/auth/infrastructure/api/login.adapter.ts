import {inject, Injectable} from '@angular/core';
import {AuthRepository} from "../../domain/ports/auth.repository";
import {catchError, Observable} from 'rxjs';
import {LoginResponse} from '../../domain/models/login-response.model';
import {Login} from '../../domain/models/login.model';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {ApiGenericResponse} from "../../../../core/model/interfaces/ApiGenericResponse";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class LoginAdapter implements AuthRepository {

  private readonly http: HttpClient = inject(HttpClient);
  private readonly baseUrl: string = 'http://localhost:8080/api/v1';

  login(credentials: Login): Observable<LoginResponse> {
    return this.http.post<ApiGenericResponse<LoginResponse>>(`${this.baseUrl}/auth/login`, credentials).pipe(
      map((response: ApiGenericResponse<LoginResponse>) => response.data),
      catchError((error: HttpErrorResponse) => {
        console.error('Error en la solicitud de inicio de sesión:', error);
        throw new Error(error.error.message);
      })
    )
  }
}
