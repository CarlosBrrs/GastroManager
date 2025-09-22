import {HttpInterceptorFn, HttpRequest} from '@angular/common/http';
import {inject} from "@angular/core";
import {AuthStore} from "../store/auth/auth.store";
import { RestaurantStore } from "../store/restaurant/restaurant.store";
import { tap } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';

// Lista blanca de URLs que NO requieren autenticación
const PUBLIC_URLS: string[] = [
  '/auth/login',
  '/auth/verify',
  '/auth/register',
  '/subscription-plans',
  // '/restaurants' // Comentado porque actualmente se necesita auth para restaurants
];

/**
 * Verifica si una URL está en la lista de URLs públicas (sin autenticación)
 * @param url URL a verificar
 * @returns true si la URL es pública, false si requiere autenticación
 */
const isPublicUrl = (url: string): boolean => {
  return PUBLIC_URLS.some(publicUrl => url.includes(publicUrl));
};

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authStore = inject(AuthStore);
  const restaurantStore = inject(RestaurantStore);
  const token = localStorage.getItem("jwt_token");
  const restaurantUuid = restaurantStore.selectedRestaurantUuid();

  // Log de la petición entrante
  console.log('🚀 [AuthInterceptor] Petición interceptada:');
  console.log('🚀 [AuthInterceptor] URL:', req.url);
  console.log('🚀 [AuthInterceptor] Método:', req.method);
  console.log('🚀 [AuthInterceptor] Headers originales:', req.headers.keys().map(key => `${key}: ${req.headers.get(key)}`));
  console.log('🚀 [AuthInterceptor] Parámetros URL:', req.params.keys().map(key => `${key}=${req.params.get(key)}`));
  console.log('🚀 [AuthInterceptor] Body:', req.body);
  console.log('🚀 [AuthInterceptor] Token disponible:', !!token);
  console.log('🚀 [AuthInterceptor] Restaurant UUID:', restaurantUuid);
  console.log('🚀 [AuthInterceptor] Es URL pública:', isPublicUrl(req.url));

  // Si hay token y la URL NO está en la lista de URLs públicas
  if (token && !isPublicUrl(req.url)) {

    const authReq: HttpRequest<any> = req.clone({
        setHeaders:
          {
            Authorization: `Bearer ${token}`,
            'X-Restaurant-Uuid': restaurantUuid || '',
          }
      });

    console.log('🔐 [AuthInterceptor] Headers agregados a la petición:');
    console.log('🔐 [AuthInterceptor] Authorization:', `Bearer ${token?.substring(0, 20)}...`);
    console.log('🔐 [AuthInterceptor] X-Restaurant-Uuid:', restaurantUuid);
    console.log('🔐 [AuthInterceptor] Headers finales:', authReq.headers.keys().map(key => `${key}: ${key === 'Authorization' ? authReq.headers.get(key)?.substring(0, 30) + '...' : authReq.headers.get(key)}`));

    return next(authReq).pipe(
      tap({
        next: (response) => {
          console.log('✅ [AuthInterceptor] Respuesta exitosa para:', req.url);
          if (response instanceof HttpResponse) {
            console.log('✅ [AuthInterceptor] Status:', response.status);
          }
        },
        error: (error) => {
          console.error('❌ [AuthInterceptor] Error en petición:', req.url);
          console.error('❌ [AuthInterceptor] Error status:', error.status);
          console.error('❌ [AuthInterceptor] Error message:', error.message);
        }
      })
    );
  }

  console.log('⚠️ [AuthInterceptor] Petición sin autenticación (URL pública):', req.url);
  return next(req).pipe(
    tap({
      next: (response) => {
        console.log('✅ [AuthInterceptor] Respuesta exitosa (sin auth) para:', req.url);
      },
      error: (error) => {
        console.error('❌ [AuthInterceptor] Error (sin auth) en petición:', req.url);
        console.error('❌ [AuthInterceptor] Error status:', error.status);
      }
    })
  );
};
