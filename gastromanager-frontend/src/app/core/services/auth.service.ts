import {Injectable, signal} from '@angular/core';
import {Observable, tap} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {LoginRequestDto} from "../../pages/login/model/interfaces/LoginRequestDto";
import {ApiGenericResponse} from "../model/interfaces/ApiGenericResponse";
// import {LoginResponseDto} from "../../pages/login/model/interfaces/LoginResponseDto";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  isLoggedIn = signal<boolean>(false);
  private tokenKey: string = "jwtToken";

  constructor(private http: HttpClient, private router: Router) {
  }

  login(loginRequestDto: LoginRequestDto): Observable<ApiGenericResponse<string>> {
    return this.http.post<ApiGenericResponse<string>>("http://localhost:8080/api/v1/auth/login", loginRequestDto, {
      headers: {'Content-Type': 'application/json'}
    }).pipe(
      tap(response => {
        if (response.flag) {
          this.setTokenInSystem(response.data);
          this.isLoggedIn.set(true);
        }
      })
    );
  }


  private setTokenInSystem(token: string): void {
    localStorage.setItem(this.tokenKey, token);
  }

  private getTokenFromSystem(): string | null{
    return localStorage.getItem(this.tokenKey);
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    this.isLoggedIn.set(false);  // Cambiamos el signal a false al cerrar sesión
    this.router.navigate(["login"]);
  }

  private validateToken(token: string | null) {
    //verifica el nulo
    if (!token) {
      return false;
    }
    // si no es nulo, dividelo en 3 guiado por el punto, si no puedes, no es valido el token
    const parts = token.split('.');
    if (parts.length !== 3) {
      return false;
    }

    // si puedes, parsea el payload y saca el exp
    try {
      const payload = JSON.parse(atob(parts[1]));
      const exp = payload.exp;

      // si no tenemos exp, no es valido
      if (!exp) {
        return false;
      }
      // si la exp es mas grande que la date de hoy, es valido
      const expirationDate = new Date(exp * 1000);
      return expirationDate > new Date();
    } catch {
      // si hay un error, false
      return false;
    }

  }
}
