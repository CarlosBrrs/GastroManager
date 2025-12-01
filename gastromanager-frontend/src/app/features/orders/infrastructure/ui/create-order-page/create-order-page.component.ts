import {ChangeDetectionStrategy, Component, computed, inject, signal, OnInit} from '@angular/core';
import {ProductSelectorComponent} from '../product-selector/product-selector.component';
import {Product} from '../../../../products/domain/models/product.interface';
import {ProductStore} from "../../../../../core/store/product/product.store";
import {OrderSummaryComponent} from "../order-summary/order-summary.component";
import {OrderItem} from "../../../domain/models/order.models";
import {OrderStore} from "../../../../../core/store/order/order.store";
import {Order} from "../../../domain/models/order.interface";
import {Router} from "@angular/router";
import {RestaurantStore} from "../../../../../core/store/restaurant/restaurant.store";

@Component({
  selector: 'gm-create-order-page',
  standalone: true,
  imports: [ProductSelectorComponent, OrderSummaryComponent],
  templateUrl: './create-order-page.component.html',
  styleUrl: './create-order-page.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CreateOrderPageComponent implements OnInit {
  orderItems = signal<OrderItem[]>([]);
  productStore = inject(ProductStore);
  orderStore = inject(OrderStore);
  restaurantStore = inject(RestaurantStore);
  router = inject(Router);

  // Exponer loading y error del OrderStore
  readonly loading = this.orderStore.loading;
  readonly error = this.orderStore.error;

  // Exponer la configuración del restaurante actual
  readonly currentRestaurantDetails = this.restaurantStore.currentRestaurantDetails;

  ngOnInit() {
    // Si no hay productos por categoría cargados, cargarlos
    if (!this.productStore.productsByCategory()) {
      this.productStore.getProductsGroupedByCategory({
        groupBy: 'category',
        includeEmpty: true,
        isEnabled: true
      });
    }
  }

  productsByCategory = computed(() => {
    return this.productStore.productsByCategory() || {};
  })

  onProductSelected(product: Product): void {
    const currentItems = this.orderItems();
    const existingItem = currentItems.find(item => item.product.uuid === product.uuid);

    if (existingItem) {
      const updatedItems = currentItems.map(item =>
        item.product.uuid === product.uuid
          ? {...item, quantity: item.quantity + 1, subtotal: (item.quantity + 1) * item.product.salePrice}
          : item
      );
      this.orderItems.set(updatedItems);
    } else {
      const newItem: OrderItem = {
        product,
        quantity: 1,
        subtotal: product.salePrice
      };
      this.orderItems.set([...currentItems, newItem]);
    }
  }

  onItemRemoved(productUuid: string): void {
    const updatedItems = this.orderItems().filter(item => item.product.uuid !== productUuid);
    this.orderItems.set(updatedItems);
  }

  onQuantityChanged(event: { productUuid: string, quantity: number }): void {
    const updatedItems = this.orderItems().map(item =>
      item.product.uuid === event.productUuid
        ? {...item, quantity: event.quantity, subtotal: event.quantity * item.product.salePrice}
        : item
    );
    this.orderItems.set(updatedItems);
  }

  onOrderSubmitted(event: {orderData: any, requiresPayment: boolean}): void {
    const { orderData, requiresPayment } = event;
    console.log('📦 Orden recibida en el padre:', orderData);
    console.log('💳 Requiere pago:', requiresPayment);

    // Crear la orden siguiendo la misma estructura que antes
    const order: Order = {
      uuid: '',
      code: '',
      customerName: orderData.customerName,
      paymentType: orderData.paymentType,
      customerNotes: orderData.orderNotes,
      tableNumber: orderData.tableNumber,
      status: 'Pending',
      totalAmount: orderData.items.reduce((sum: number, item: any) => sum + item.subtotal, 0),
      totalPaid: 0, // Nueva orden no tiene pagos iniciales
      remainingToPay: orderData.items.reduce((sum: number, item: any) => sum + item.subtotal, 0), // El monto completo está pendiente
      paymentStatus: 'UNPAID', // Nueva orden está sin pagar
      orderItems: orderData.items.map((item: any) => {
        return {
          productUuid: item.productUuid,
          productName: item.productName,
          quantity: item.quantity,
          customerNotes: item.notes,
        }
      })
    };

    // Siempre enviar la orden al backend
    this.orderStore.createOrder(order).subscribe({
      next: (createdOrder) => {
        console.log('✅ Orden creada exitosamente:', createdOrder);
        this.orderItems.set([]);

        // Manejar redirección según si requiere pago
        if (requiresPayment) {
          console.log('🔄 Redirigiendo a página de pago...');

          // Redirigir a página de pago solo con orderUuid
          this.router.navigate(['/payments'], {
            queryParams: {
              orderUuid: createdOrder
            }
          });
        } else {
          console.log('✅ Orden completada - redirigiendo a órdenes');
          // Flujo normal: redirigir a la lista de órdenes
          this.router.navigate(['/orders']);
        }
      },
      error: (error) => {
        console.error('❌ Error al crear la orden:', error);
        const errorMessage = error?.error?.message || error?.message || 'Error desconocido al crear la orden';
        alert(`Error al crear la orden: ${errorMessage}`);
      }
    });
  }
}
