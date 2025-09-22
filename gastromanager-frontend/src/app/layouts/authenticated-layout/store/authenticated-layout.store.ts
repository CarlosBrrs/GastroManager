import {patchState, signalStore, withHooks, withState} from "@ngrx/signals";
import {inject} from "@angular/core";
import {NavigationEnd, Router} from "@angular/router";
import {filter} from "rxjs/operators";
import {tap} from "rxjs";

// todo mover a un layout service
const sidebarLinks: Record<string, SidebarLink[]> = {
  ingredients: [
    {label: 'Ver ingredientes', route: '/ingredients'},
    {label: 'Agregar ingrediente', route: '/ingredients/create'}
  ],
  users: [
    {label: 'Listado de usuarios', route: '/users/list'},
    {label: 'Crear usuario', route: '/users/create'}
  ],
  menus: [
    {label: 'Ver menus', route: '/menus'},
    {label: 'Agregar menu', route: '/menus/create'}
  ],
  products: [
    {label: 'Ver productos', route: '/products'},
    {label: 'Agregar producto', route: '/products/create'}
  ],
  orders: [
    {label: 'Ver ordenes', route: '/orders'},
    {label: 'Crear nueva orden', route: '/orders/create'}
  ],
  management: [
    {label: 'Mesas', route: '/management/tables'},
    {label: 'Cajas registradoras', route: '/management/cash-registers'},
    {label: 'Configuración', route: '/management/settings'},
    {label: 'Reportes', route: '/management/reports'},
    {label: 'Estado del restaurante', route: '/management/status'}
  ],
  // otros módulos...
};

export interface SidebarLink {
  label: string;
  route: string;
}

type LayoutState = {
  moduleTitle: string;
  showSecondarySidebar: boolean;
  secondarySidebarLinks: SidebarLink[];
};

const initialState: LayoutState = {
  moduleTitle: '',
  showSecondarySidebar: false,
  secondarySidebarLinks: [],
};

export const LayoutStore = signalStore(
  {providedIn: 'root'},
  withState(initialState),
  withHooks({
    onInit(store) {
      const router = inject(Router);
      router.events
        .pipe(
          filter(event => event instanceof NavigationEnd),
          tap(() => {
            const url = router.url; // e.g. /ingredients-page/list
            const moduleSegment = url.split('/')[1]; // "ingredients-page"
            const title = capitalize(moduleSegment.replace('-', ' ')); // opcional
            const links = sidebarLinks[moduleSegment] as SidebarLink[] || [];

            patchState(store, {
              moduleTitle: title,
              showSecondarySidebar: links.length > 0,
              secondarySidebarLinks: links
            });
          })
        ).subscribe()
    }
  })
);

function capitalize(str: string): string {
  return str.charAt(0).toUpperCase() + str.slice(1);
}
