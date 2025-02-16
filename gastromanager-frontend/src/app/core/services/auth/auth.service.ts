import {Injectable, signal} from '@angular/core';
import {Observable, tap} from "rxjs";
import {LoginRequestDto} from "../../../pages/login/model/interfaces/LoginRequestDto";
import {ApiGenericResponse} from "../../model/interfaces/ApiGenericResponse";
import {Router} from "@angular/router";
import {jwtDecode} from 'jwt-decode';
import {BaseHttpService} from "../basehttp/base-http.service";
import {DecodedToken} from "../../model/interfaces/DecodedToken";
import {SignupRequestDto} from "../../../pages/register/register.component";

interface RestaurantDto {
  name: string;
  uuid: string;
}

interface RestaurantDto {
  uuid: string;
  // ...otros campos del restaurante
}

interface LoginResponseDto {
  jwtToken: string;
  restaurants?: RestaurantDto[];      // Opcional: lista de restaurantes
  restaurantUuid?: string;            // Opcional: único restaurantUuid
}

@Injectable({
  providedIn: 'root'
})
export class AuthService extends BaseHttpService {

  isLoggedIn;
  private readonly tokenKey: string = "jwtToken";
  private readonly userInfoKey: string = "userInfo";
  private roles: string[] = [];
  private readonly restaurantUuidKey: string = "restaurantUuid";

  constructor(private readonly router: Router) {
    super();
    this.isLoggedIn = signal<boolean>(this.hasToken());
    this.loadRolesFromToken();
  }

  login(loginRequestDto: LoginRequestDto): Observable<ApiGenericResponse<LoginResponseDto>> {
    return this.http.post<ApiGenericResponse<LoginResponseDto>>(`${this.apiUrl}/auth/login`, loginRequestDto, {
      headers: {'Content-Type': 'application/json'}
    }).pipe(
      // for sideffects
      tap(response => {
        if (response.flag) {
          const token = response.data.jwtToken;
          // TODO: LA ESTRUCTURA VA A VARIAR SI ES OWNER O MANAGER, AJUSTAR
          const restaurantUuid = response.data.restaurants?.[0]?.uuid ?? response.data.restaurantUuid ?? "";
          console.log(token)
          console.log("restaurantuuid", restaurantUuid)
          const decodedToken: DecodedToken = jwtDecode<DecodedToken>(token);
          this.roles = decodedToken.roles;
          this.setTokenInSystem(token);
          this.setRestaurantUuidInSystem(restaurantUuid);
          this.isLoggedIn.set(this.hasToken());
        }
      })
    );
  }

  signup(signupRequestDto: SignupRequestDto) {
    return this.handleRequest<string>("POST", "auth/sign-up", signupRequestDto);
  }

  verifyAccount(token: string) {
    return this.handleRequest<string>("GET", `auth/verify-account?token=${token}`);
  }

  getUserUuid(): string {
    const tokenFromSystem = this.getTokenFromSystem();
    if (tokenFromSystem) {
      const decodedToken: DecodedToken = jwtDecode<DecodedToken>(tokenFromSystem);
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
    localStorage.removeItem(this.userInfoKey);
    this.isLoggedIn.set(false);  // Cambiamos el signal a false al cerrar sesión
    this.router.navigate(["login"]);
  }

  private loadRolesFromToken(): void {
    const token = localStorage.getItem(this.tokenKey);
    if (token) {
      const decodedToken: DecodedToken = jwtDecode<DecodedToken>(token);
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

  private setRestaurantUuidInSystem(restaurantUuid: string) {
    localStorage.setItem(this.restaurantUuidKey, restaurantUuid);
  }

  private getRestaurantUuidFromSystem(): string | null {
    return localStorage.getItem(this.restaurantUuidKey);
  }


}
