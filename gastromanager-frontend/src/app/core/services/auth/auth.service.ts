import {Injectable, signal} from '@angular/core';
import {Observable, tap} from "rxjs";
import {LoginRequestDto} from "../../../pages/login/model/interfaces/LoginRequestDto";
import {ApiGenericResponse} from "../../model/interfaces/ApiGenericResponse";
import {Router} from "@angular/router";
import {jwtDecode} from 'jwt-decode';
import {BaseHttpService} from "../basehttp/base-http.service";

@Injectable({
  providedIn: 'root'
})
export class AuthService extends BaseHttpService {

  isLoggedIn;
  private tokenKey: string = "jwtToken";
  private roles: string[] = [];

  constructor(private router: Router) {
    super();
    this.isLoggedIn = signal<boolean>(this.hasToken());
    this.loadRolesFromToken();
  }

  login(loginRequestDto: LoginRequestDto): Observable<ApiGenericResponse<string>> {
    return this.http.post<ApiGenericResponse<string>>("http://localhost:8080/api/v1/auth/login", loginRequestDto, {
      headers: {'Content-Type': 'application/json'}
    }).pipe(
      // for sideffects
      tap(response => {
        if (response.flag) {
          const token = response.data;
          const decodedToken: any = jwtDecode(token);
          this.roles = decodedToken.roles;
          this.setTokenInSystem(token);
          this.isLoggedIn.set(this.hasToken());
        }
      })
    );
  }

  getUserUuid(): string {
    const tokenFromSystem = this.getTokenFromSystem();
    if (tokenFromSystem) {
      const decodedToken: any = jwtDecode(tokenFromSystem);
      return decodedToken.uuid;
    }
    return "";
  }

  getRoles(): string[] {
    return this.roles;
  }

  hasRole(role: string): boolean {
    return this.roles.includes(role);
  }

  hasAnyRole(roles: string[]): boolean {
    return roles.some(role => this.roles.includes(role));
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    this.isLoggedIn.set(false);  // Cambiamos el signal a false al cerrar sesión
    this.router.navigate(["login"]);
  }

  private loadRolesFromToken(): void {
    const token = localStorage.getItem(this.tokenKey);
    if (token) {
      const decodedToken: any = jwtDecode(token);
      this.roles = decodedToken.roles || [];
    }
  }

  private setTokenInSystem(token: string): void {
    localStorage.setItem(this.tokenKey, token);
  }

  private getTokenFromSystem(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  private validateToken(token: string | null): boolean {
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

  private hasToken(): boolean {
    const tokenInStorage = localStorage.getItem(this.tokenKey);
    return !!tokenInStorage && this.validateToken(tokenInStorage);
  }
}
