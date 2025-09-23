import {inject, Injectable} from "@angular/core";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {catchError, Observable} from "rxjs";
import {ApiGenericResponse} from "../../../../core/model/interfaces/ApiGenericResponse";
import {map} from "rxjs/operators";
import {UserRepository} from "../../domain/ports/user.repository";
import {User} from "../../../../core/store/auth/auth.store";
import {environment} from "../../../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class UserAdapter implements UserRepository {

  private readonly http: HttpClient = inject(HttpClient);
  private readonly baseUrl: string = environment.API_URL;

  getUserByUsername(uuid: string): Observable<User> {
    return this.http.get<ApiGenericResponse<User>>(`${this.baseUrl}/users/${uuid}`).pipe(
      map((response: ApiGenericResponse<User>) => response.data),
      catchError((error: HttpErrorResponse) => {
        console.error('Error en la solicitud de usuario:', error);
        throw new Error(error.error.message);
      })
    )
  }
}
