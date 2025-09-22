import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { AuthStore } from "../../../../../core/store/auth/auth.store";
import {RestaurantStore} from "../../../../../core/store/restaurant/restaurant.store";
import {JsonPipe} from "@angular/common";

// Interfaces para datos hardcodeados
interface DashboardStats {
  dailySales: number;
  activeOrders: number;
  popularProduct: string;
  popularProductCount: number;
  activeTable: string;
}

interface RecentOrder {
  id: string;
  table: string;
  total: number;
  status: 'pending' | 'preparing' | 'ready' | 'delivered';
  time: string;
}

interface PopularProduct {
  name: string;
  sold: number;
  revenue: number;
}

@Component({
  selector: 'gm-dashboard-page',
  standalone: true,
  imports: [
    JsonPipe
  ],
  templateUrl: './dashboard-page.component.html',
  styleUrl: './dashboard-page.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class DashboardPageComponent {
  private readonly authStore = inject(AuthStore);
  restaurantStore = inject(RestaurantStore);
  selectedRestaurant = this.restaurantStore.currentRestaurantDetails
  // readonly restaurant = computed(() => this.authStore.selectedRestaurant());

  // Estado del dashboard (datos hardcodeados)
  readonly stats = signal<DashboardStats>({
    dailySales: 2845.50,
    activeOrders: 12,
    popularProduct: 'Hamburguesa Clásica',
    popularProductCount: 23,
    activeTable: 'Mesa #7'
  });

  readonly recentOrders = signal<RecentOrder[]>([
    { id: '#001', table: 'Mesa 3', total: 45.50, status: 'preparing', time: '14:30' },
    { id: '#002', table: 'Mesa 7', total: 67.80, status: 'ready', time: '14:25' },
    { id: '#003', table: 'Mesa 1', total: 32.20, status: 'pending', time: '14:20' },
    { id: '#004', table: 'Mesa 5', total: 89.90, status: 'delivered', time: '14:15' }
  ]);

  readonly popularProducts = signal<PopularProduct[]>([
    { name: 'Hamburguesa Clásica', sold: 23, revenue: 367.70 },
    { name: 'Pizza Margherita', sold: 18, revenue: 225.00 },
    { name: 'Pasta Carbonara', sold: 15, revenue: 217.50 },
    { name: 'Ensalada César', sold: 12, revenue: 107.88 }
  ]);

  // Métodos para manejar acciones
  onViewOrderDetails(orderId: string): void {
    console.log('Ver detalles de orden:', orderId);
  }

  onCreateNewOrder(): void {
    console.log('Crear nueva orden');
  }

  onViewInventory(): void {
    console.log('Ver inventario');
  }
}
