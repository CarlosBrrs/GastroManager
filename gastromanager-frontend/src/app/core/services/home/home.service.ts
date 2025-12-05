import {Injectable} from '@angular/core';
import {BaseHttpService} from "../basehttp/base-http.service";

@Injectable({
  providedIn: 'root'
})
export class HomeService extends BaseHttpService {

  constructor() {
    super();
  }

  /*getKeyMetrics(): Observable<KeyMetric[]> {
    // const role = this.authService.getUserRole();
    // const restaurantId = this.authService.getRestaurantId();
    const role: string = 'ROLE_SUPERUSER';
    switch (role) {
      case 'ROLE_SUPERUSER':
        return this.http.get<KeyMetric[]>('/api/metrics/platform');
      case 'ROLE_OWNER':
      // return this.http.get<KeyMetric[]>(`/api/metrics/owner/${this.authService.getUserId()}`);
      case 'ROLE_MANAGER':
      case 'ROLE_CHEF':
      case 'ROLE_WAITER':
      case 'ROLE_CASHIER':
        // return this.http.get<KeyMetric[]>(`/api/metrics/restaurant/${restaurantId}`);
        return of([]);
      default:
        return of([]);
    }
  }*/

  /*  getQuickActions(): QuickAction[] {
      // const role = this.authService.getUserRole();
      const role: string = 'ROLE_SUPERUSER';
      const actions: Record<string, QuickAction[]> = {
        ROLE_SUPERUSER: [
          { label: 'Add Restaurant', icon: 'pi pi-plus', route: '/restaurants/new' },
          { label: 'Platform Settings', icon: 'pi pi-cog', route: '/settings' },
          { label: 'User Management', icon: 'pi pi-users', route: '/users' },
          { label: 'Reports', icon: 'pi pi-chart-bar', route: '/reports' }
        ],
        ROLE_OWNER: [
          { label: 'Add Restaurant', icon: 'pi pi-plus', route: '/restaurants/new' },
          { label: 'View Reports', icon: 'pi pi-chart-bar', route: '/reports' },
          { label: 'Manage Staff', icon: 'pi pi-users', route: '/staff' },
          { label: 'Settings', icon: 'pi pi-cog', route: '/settings' }
        ],
        ROLE_MANAGER: [
          { label: 'New Order', icon: 'pi pi-plus', route: '/orders/new' },
          { label: 'Reservations', icon: 'pi pi-calendar', route: '/reservations' },
          { label: 'Inventory', icon: 'pi pi-box', route: '/inventory' },
          { label: 'Staff Schedule', icon: 'pi pi-clock', route: '/schedule' }
        ],
        ROLE_CHEF: [
          { label: 'View Orders', icon: 'pi pi-list', route: '/orders' },
          { label: 'Inventory', icon: 'pi pi-box', route: '/inventory' },
          { label: 'Menu Items', icon: 'pi pi-book', route: '/menu' }
        ],
        ROLE_WAITER: [
          { label: 'New Order', icon: 'pi pi-plus', route: '/orders/new' },
          { label: 'Active Tables', icon: 'pi pi-table', route: '/tables' },
          { label: 'View Menu', icon: 'pi pi-book', route: '/menu' }
        ],
        ROLE_CASHIER: [
          { label: 'New Payment', icon: 'pi pi-dollar', route: '/payments/new' },
          { label: 'Open Bills', icon: 'pi pi-file', route: '/bills' },
          { label: 'Daily Report', icon: 'pi pi-chart-bar', route: '/reports/daily' }
        ]
      };
      return actions[role] || [];
    }*/
}
