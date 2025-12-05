import {patchState, signalStore, withHooks, withMethods, withState} from "@ngrx/signals";
import {Login} from "../../../features/auth/domain/models/login.model";
import {inject} from "@angular/core";
import {tapResponse} from "@ngrx/operators";
import {rxMethod} from "@ngrx/signals/rxjs-interop";
import {pipe, switchMap, tap} from "rxjs";
import {LoginUseCase} from "../../../features/auth/application/usecases/login.use-case";
import {LoginResponse} from "../../../features/auth/domain/models/login-response.model";
import {HttpErrorResponse} from "@angular/common/http";
import {GetUserUseCase} from "../../../features/user/application/usecases/get-user.use-case";

interface Role {
  uuid: string;
  name: string;
}

export interface User {
  id: string;
  name: string;
  lastname: string;
  username: string;
  email: string;
  roles: Role[];
}

type AuthState = {
  user: User | null;
  isAuthenticated: boolean;
  loading: boolean;
  error: string | null;
}

const initialState: AuthState = {
  user: null,
  isAuthenticated: false,
  loading: false,
  error: null,
}

function extractUuidFromToken(token: string): string {
  const payload = JSON.parse(atob(token.split('.')[1]));
  return payload.uuid;
}

export const AuthStore = signalStore(
  {providedIn: "root",},
  withState(initialState),
  withMethods((store,
               loginUseCase = inject(LoginUseCase),
               getUserUseCase = inject(GetUserUseCase)) => ({
    login: rxMethod<Login>(
      pipe(
        tap(() => patchState(store, {loading: true, error: null})),
        switchMap((credentials: Login) =>
          loginUseCase.execute(credentials).pipe(
            tap((data: LoginResponse) => {
              localStorage.setItem('jwt_token', data.jwtToken);
              console.log("login response:", data);
              patchState(store, {
                isAuthenticated: true,
              });
            }),
            switchMap((data: LoginResponse) => {
              const token = data.jwtToken;
              const uuid = extractUuidFromToken(token);
              return getUserUseCase.execute(uuid);
            }),
            tapResponse({
              next: (user: User) => {
                console.log("user response:", user);
                localStorage.setItem('userInfo', JSON.stringify(user));
                patchState(store, {user});
              },
              error: (error: HttpErrorResponse) => {
                const message = error.message || 'Error desconocido';
                patchState(store, {error: message});
                console.error("Login error:", error);
              },
              finalize: () => patchState(store, {loading: false})
            })
          )
        )
      )
    ),
    logout: () => {
      localStorage.removeItem('jwt_token');
      localStorage.removeItem('userInfo');
      localStorage.removeItem('selectedRestaurantUuid');

      patchState(store, {
        user: null,
        isAuthenticated: false,
        error: null,
        loading: false
      });
    },
  })),
  withHooks(
    {
      onInit: (store) => {
        if (localStorage.getItem('jwt_token') && localStorage.getItem('userInfo')) {
          const userInfo = localStorage.getItem('userInfo');
          const user = JSON.parse(userInfo!);
          patchState(store, {
            user,
            isAuthenticated: true,
            loading: false,
            error: null
          });
        }
      }
    }
  )
);
