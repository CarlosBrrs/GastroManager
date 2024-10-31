import {HttpInterceptorFn, HttpRequest} from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {

  const token: string | null = localStorage.getItem("jwtToken");

  if (token && !req.url.includes("/auth/login")) {
    const authReq: HttpRequest<any> = req.clone({
      setHeaders:
        {
          Authorization: `Bearer ${token}`
        }
    });
    return next(authReq);
  }
  return next(req);
};
