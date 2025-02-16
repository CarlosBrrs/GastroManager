import {HttpInterceptorFn, HttpRequest} from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {

  const token: string | null = localStorage.getItem("jwtToken");
  const restaurantUuid: string | null = localStorage.getItem("restaurantUuid");

  if (token && !req.url.includes("/auth/login") &&
    !req.url.includes("/auth/verify") &&
    !req.url.includes("/subscription-plans") &&
    !req.url.includes("/auth/register")) {
    const authReq: HttpRequest<any> = req.clone({
      setHeaders:
        {
          Authorization: `Bearer ${token}`,
          'X-Restaurant-Uuid': restaurantUuid ?? ""
        }
  })
    ;
    return next(authReq);
  }
  return next(req);
};
