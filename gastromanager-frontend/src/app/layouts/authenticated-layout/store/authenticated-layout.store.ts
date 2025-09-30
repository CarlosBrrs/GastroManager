import {patchState, signalStore, withHooks, withState, withComputed} from "@ngrx/signals";
import {inject, computed, effect} from "@angular/core";
import {NavigationEnd, Router} from "@angular/router";
import {filter} from "rxjs/operators";
import {tap} from "rxjs";
import {CashRegisterStore} from "../../../core/store/cash-register/cash-register.store";
import {RestaurantStore} from "../../../core/store/restaurant/restaurant.store";

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
  reports: [
    {label: 'Ventas', route: '/reports/sales'},
    {label: 'Inventario', route: '/reports/inventory'},
    {label: 'Clientes', route: '/reports/customers'},
    {label: 'Desempeño', route: '/reports/performance'}
  ],
  // otros módulos...
};

// Filtros dinámicos por módulo
const moduleFilters: Record<string, FilterConfig[]> = {
  ingredients: [
    { type: 'search', placeholder: 'Buscar ingrediente...', key: 'name' },
    { type: 'select', label: 'Categoría', key: 'category', options: ['Carnes', 'Vegetales', 'Lácteos', 'Condimentos'] },
    { type: 'select', label: 'Estado', key: 'status', options: ['Disponible', 'Agotado', 'Por vencer'] }
  ],
  products: [
    { type: 'search', placeholder: 'Buscar producto...', key: 'name' },
    { type: 'select', label: 'Categoría', key: 'category', options: ['Entradas', 'Platos principales', 'Postres', 'Bebidas'] },
    { type: 'range', label: 'Rango de precio', key: 'price', min: 0, max: 100000 }
  ],
  orders: [
    { type: 'search', placeholder: 'Buscar orden...', key: 'code' },
    { type: 'select', label: 'Estado', key: 'status', options: ['Pendiente', 'En preparación', 'Listo', 'Entregado'] },
    { type: 'date', label: 'Fecha', key: 'date' },
    { type: 'select', label: 'Mesa', key: 'table', options: ['Mesa 1', 'Mesa 2', 'Mesa 3', 'Para llevar'] }
  ],
  users: [
    { type: 'search', placeholder: 'Buscar usuario...', key: 'name' },
    { type: 'select', label: 'Rol', key: 'role', options: ['Admin', 'Mesero', 'Cocinero', 'Cajero'] },
    { type: 'select', label: 'Estado', key: 'status', options: ['Activo', 'Inactivo'] }
  ],
  menus: [
    { type: 'search', placeholder: 'Buscar menú...', key: 'name' },
    { type: 'select', label: 'Tipo', key: 'type', options: ['Desayuno', 'Almuerzo', 'Cena', 'Especiales'] },
    { type: 'select', label: 'Estado', key: 'status', options: ['Activo', 'Inactivo'] }
  ],
  reports: [
    { type: 'date', label: 'Fecha inicio', key: 'dateFrom' },
    { type: 'date', label: 'Fecha fin', key: 'dateTo' },
    { type: 'select', label: 'Tipo de reporte', key: 'reportType', options: ['Diario', 'Semanal', 'Mensual'] },
    { type: 'select', label: 'Categoría', key: 'category', options: ['Ventas', 'Productos', 'Clientes'] }
  ],
  // Filtros específicos para reportes de ventas
  'reports/sales': [
    { type: 'date', label: 'Fecha desde', key: 'dateFrom' },
    { type: 'date', label: 'Fecha hasta', key: 'dateTo' },
    { type: 'multiselect', label: 'Cajas', key: 'cashRegister', options: ['Caja Principal', 'Caja Secundaria', 'Caja Express', 'Caja Drive-Thru'] },
    { type: 'multiselect', label: 'Sesiones', key: 'sessions', options: ['Abiertas', 'Cerradas'] }
  ],
  // Filtros específicos para reportes de inventario
  'reports/inventory': [
    { type: 'search', placeholder: 'Buscar producto...', key: 'productName' },
    { type: 'select', label: 'Categoría', key: 'category', options: ['Carnes', 'Vegetales', 'Lácteos', 'Condimentos', 'Bebidas'] },
    { type: 'select', label: 'Estado stock', key: 'stockStatus', options: ['Stock alto', 'Stock medio', 'Stock bajo', 'Sin stock'] },
    { type: 'date', label: 'Fecha desde', key: 'dateFrom' },
    { type: 'date', label: 'Fecha hasta', key: 'dateTo' },
    { type: 'select', label: 'Almacén', key: 'warehouse', options: ['Principal', 'Secundario', 'Refrigerado'] }
  ],
  // Filtros específicos para reportes de clientes
  'reports/customers': [
    { type: 'search', placeholder: 'Buscar cliente...', key: 'customerName' },
    { type: 'select', label: 'Tipo cliente', key: 'customerType', options: ['VIP', 'Regular', 'Nuevo', 'Frecuente'] },
    { type: 'select', label: 'Segmento', key: 'segment', options: ['Premium', 'Estándar', 'Básico'] },
    { type: 'range', label: 'Total gastado', key: 'totalSpent', min: 0, max: 1000000 },
    { type: 'date', label: 'Última visita desde', key: 'lastVisitFrom' },
    { type: 'select', label: 'Canal preferido', key: 'preferredChannel', options: ['Presencial', 'Delivery', 'Takeaway'] }
  ],
  // Filtros específicos para reportes de desempeño
  'reports/performance': [
    { type: 'select', label: 'Área', key: 'area', options: ['Cocina', 'Servicio', 'Caja', 'General'] },
    { type: 'select', label: 'Período', key: 'period', options: ['Hoy', 'Esta semana', 'Este mes', 'Trimestre'] },
    { type: 'select', label: 'Métrica', key: 'metric', options: ['Tiempo promedio', 'Satisfacción', 'Eficiencia', 'Productividad'] },
    { type: 'select', label: 'Empleado', key: 'employee', options: ['Todos', 'Juan Pérez', 'María García', 'Carlos López'] },
    { type: 'range', label: 'Puntuación', key: 'score', min: 1, max: 10 },
    { type: 'select', label: 'Turno', key: 'shift', options: ['Mañana', 'Tarde', 'Noche', 'Todos'] }
  ],
  management: [
    { type: 'search', placeholder: 'Buscar...', key: 'general' },
    { type: 'select', label: 'Área', key: 'area', options: ['Mesas', 'Cajas', 'Configuración'] }
  ]
};

export interface FilterConfig {
  type: 'search' | 'select' | 'date' | 'range' | 'multiselect';
  label?: string;
  placeholder?: string;
  key: string;
  options?: string[];
  min?: number;
  max?: number;
  defaultValue?: any; // Nuevo atributo para valor inicial
}

export interface SidebarLink {
  label: string;
  route: string;
}

type LayoutState = {
  moduleTitle: string;
  showSecondarySidebar: boolean;
  secondarySidebarLinks: SidebarLink[];
  moduleFilters: FilterConfig[];
  currentModule: string;
};

const initialState: LayoutState = {
  moduleTitle: '',
  showSecondarySidebar: false,
  secondarySidebarLinks: [],
  moduleFilters: [],
  currentModule: ''
};

export const LayoutStore = signalStore(
  {providedIn: 'root'},
  withState(initialState),
  withComputed((store, cashRegisterStore = inject(CashRegisterStore)) => {

    return {
      // Computed para obtener nombres de cajas registradoras dinámicamente
      cashRegisterOptions: computed(() => {
        const cashRegisters = cashRegisterStore.cashRegisters();
        return cashRegisters.map((cr: any) => cr.name);
      }),

      // Computed para obtener filtros dinámicos basados en el módulo actual
      dynamicModuleFilters: computed(() => {
        const currentModule = store.currentModule();
        const cashRegisterNames = cashRegisterStore.cashRegisters().map((cr: any) => cr.name);

        // Si estamos en reports/sales, usar datos dinámicos para cajas
        if (currentModule === 'reports/sales') {
          // Calcular fechas por defecto: día anterior y día actual
          const today = new Date();
          const yesterday = new Date(today);
          yesterday.setDate(today.getDate() - 1);

          // Formatear fechas para input tipo date (YYYY-MM-DD)
          const formatDate = (date: Date) => date.toISOString().split('T')[0];

          return [
            {
              type: 'date',
              label: 'Fecha desde',
              key: 'dateFrom',
              defaultValue: formatDate(yesterday)
            },
            {
              type: 'date',
              label: 'Fecha hasta',
              key: 'dateTo',
              defaultValue: formatDate(today)
            },
            {
              type: 'multiselect',
              label: 'Cajas',
              key: 'cashRegister',
              options: cashRegisterNames
            },
            {
              type: 'multiselect',
              label: 'Sesiones',
              key: 'sessions',
              options: ['Abiertas', 'Cerradas']
            }
          ] as FilterConfig[];
        }

        // Para otros módulos, usar filtros estáticos por ahora
        return moduleFilters[currentModule] || [];
      })
    };
  }),
  withHooks({
    onInit(store) {
      const router = inject(Router);
      const cashRegisterStore = inject(CashRegisterStore);
      const restaurantStore = inject(RestaurantStore);

      // Effect para cargar cajas registradoras SOLO cuando hay un restaurante seleccionado
      effect(() => {
        const selectedRestaurantUuid = restaurantStore.selectedRestaurantUuid();
        const currentRestaurant = restaurantStore.currentRestaurantDetails();

        console.log('🏪 [LayoutStore] Restaurant effect - UUID:', selectedRestaurantUuid, 'Details:', !!currentRestaurant);

        // Solo cargar cajas si hay un restaurante seleccionado Y tiene detalles cargados
        if (selectedRestaurantUuid && currentRestaurant) {
          console.log('✅ [LayoutStore] Restaurant available, loading cash registers...');
          cashRegisterStore.getAllCashRegisters();
        }
      }, {allowSignalWrites: true});

      router.events
        .pipe(
          filter(event => event instanceof NavigationEnd),
          tap(() => {
            const url = router.url; // e.g. /reports/inventory
            let moduleSegment = url.split('/')[1]; // "reports"
            let filterKey = moduleSegment;

            // Para rutas anidadas como /reports/inventory, usar la ruta completa
            if (moduleSegment === 'reports' && url.split('/')[2]) {
              filterKey = `${moduleSegment}/${url.split('/')[2]}`;
            }

            const title = capitalize(moduleSegment.replace('-', ' '));
            const links = sidebarLinks[moduleSegment] || [];

            patchState(store, {
              moduleTitle: title,
              showSecondarySidebar: links.length > 0,
              secondarySidebarLinks: links,
              currentModule: filterKey,
              // Actualizar moduleFilters directamente aquí cuando cambia la ruta
              moduleFilters: store.dynamicModuleFilters()
            });
          })
        ).subscribe()
    }
  })
);

function capitalize(str: string): string {
  return str.charAt(0).toUpperCase() + str.slice(1);
}
