import {ApplicationConfig, provideZoneChangeDetection} from '@angular/core';
import {provideRouter} from '@angular/router';

import {routes2} from './app.routes';
import {provideHttpClient, withInterceptors} from "@angular/common/http";
import {authInterceptor} from "./core/interceptors/auth.interceptor";
import {provideAnimationsAsync} from "@angular/platform-browser/animations/async";
import {LoginAdapter} from "./features/auth/infrastructure/api/login.adapter";
import {IngredientsAdapter} from "./features/ingredients/infrastructure/api/ingredients.adapter";

export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(withInterceptors([authInterceptor])),
    provideZoneChangeDetection({eventCoalescing: true}),
    // provideRouter(routes),
    provideRouter(routes2),
    provideAnimationsAsync(),
    LoginAdapter,
    IngredientsAdapter]
};
