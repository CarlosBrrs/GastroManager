import {Observable} from "rxjs";
import {LoginResponse} from "../models/login-response.model";
import {Login} from "../models/login.model";

export interface AuthRepository {
  login(credentials: Login): Observable<LoginResponse>;
}
