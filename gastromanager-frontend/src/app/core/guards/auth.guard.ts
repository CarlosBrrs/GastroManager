import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {AuthService} from "../services/auth.service";

export const authGuard: CanActivateFn = (route, state) => {

  const router = inject(Router);
  const authService = inject(AuthService);

  if (authService.isLoggedIn()) {
    return true; // Si está autenticado, permitir el acceso
  } else {
    router.navigate(['login']).then(r => console.log("token validation error",r));
    return false;
  }
};
