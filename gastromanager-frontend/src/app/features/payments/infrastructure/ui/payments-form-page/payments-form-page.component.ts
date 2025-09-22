import {ChangeDetectionStrategy, Component, inject, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {CommonModule} from '@angular/common';
import {PaymentFormComponent} from '../payment-form/payment-form.component';
import {OrderStore} from '../../../../../core/store/order/order.store';
import {RestaurantStore} from '../../../../../core/store/restaurant/restaurant.store';
import {PaymentStore} from '../../../../../core/store/payment/payment.store';

// Interface para los datos del pago (actualizada para coincidir con PaymentCreateRequestDto del backend)
interface PaymentData {
  amount: number;           // Corresponde a BigDecimal amount
  paymentMethod: string;    // Corresponde a PaymentMethod paymentMethod
  tipAmount: number;        // Corresponde a BigDecimal tipAmount
  notes?: string;           // Corresponde a String notes (opcional)
  orderUuid: string;        // Corresponde a UUID orderUuid
  transactionId?: string;   // Corresponde a String transactionId (opcional)
}

@Component({
  selector: 'gm-payments-form-page',
  standalone: true,
  imports: [CommonModule, PaymentFormComponent],
  templateUrl: './payments-form-page.component.html',
  styleUrl: './payments-form-page.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PaymentsFormPageComponent implements OnInit, OnDestroy {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly orderStore = inject(OrderStore);
  private readonly restaurantStore = inject(RestaurantStore);
  private readonly paymentStore = inject(PaymentStore);

  // Exponer signals del OrderStore
  readonly loading = this.orderStore.loading;
  readonly error = this.orderStore.error;
  readonly currentOrder = this.orderStore.currentOrder;

  // Exponer signals del PaymentStore
  readonly paymentLoading = this.paymentStore.loading;
  readonly paymentError = this.paymentStore.error;
  readonly lastPaymentResponseUuid = this.paymentStore.lastPaymentResponseUuid;

  ngOnInit() {
    console.log('🚀 [PaymentsFormPageComponent] Inicializando componente de pagos');

    // Limpiar errores de pagos previos al inicializar el componente
    this.paymentStore.clearError();
    console.log('🧹 [PaymentsFormPageComponent] Errores de pago limpiados al inicializar');

    this.route.queryParams.subscribe(params => {
      console.log('📋 [PaymentsFormPageComponent] Query params recibidos:', params);
      const orderUuidFromParams = params['orderUuid'];
      console.log('🔍 [PaymentsFormPageComponent] OrderUuid extraído:', orderUuidFromParams);

      if (orderUuidFromParams) {
        console.log('✅ [PaymentsFormPageComponent] UUID válido encontrado, esperando inicialización del restaurant...');
        // Limpiar errores antes de cargar una nueva orden
        this.paymentStore.clearError();
        this.waitForRestaurantAndLoadOrder(orderUuidFromParams);
      } else {
        console.warn('⚠️ [PaymentsFormPageComponent] No se proporcionó orderUuid en los parámetros de consulta');
        console.log('📊 [PaymentsFormPageComponent] Parámetros disponibles:', Object.keys(params));
      }
    });
  }

  /**
   * Maneja el evento de procesamiento de pago emitido por el formulario
   */
  onProcessPayment(paymentData: PaymentData): void {
    console.log('💳 [PaymentsFormPageComponent] Recibiendo datos de pago del formulario:', paymentData);

    // Limpiar errores previos
    this.paymentStore.clearError();

    // Procesar el pago y suscribirse directamente al resultado
    this.paymentStore.processPayment(paymentData).subscribe({
      next: (paymentResponse) => {
        console.log('✅ [PaymentsFormPageComponent] Pago completado exitosamente:', paymentResponse);
        this.paymentStore.clearPaymentState();
        this.router.navigate(['/orders']);
      },
      error: (error) => {
        console.error('❌ [PaymentsFormPageComponent] Error al procesar pago:', error);
        // El error ya se maneja en el PaymentStore, no necesitamos hacer nada adicional aquí
      }
    });
  }

  /**
   * Espera a que el RestaurantStore esté inicializado y luego carga la orden
   */
  private waitForRestaurantAndLoadOrder(orderUuid: string): void {
    // Crear un interval que verifique el estado del restaurant store
    const checkRestaurantInterval = setInterval(() => {
      const restaurantUuid = this.restaurantStore.selectedRestaurantUuid();
      const restaurantDetails = this.restaurantStore.currentRestaurantDetails();

      console.log('🔄 [PaymentsFormPageComponent] Verificando estado del restaurant - UUID:', restaurantUuid, 'Details:', !!restaurantDetails);

      if (restaurantUuid && restaurantUuid.trim() !== '') {
        console.log('✅ [PaymentsFormPageComponent] Restaurant UUID disponible, cargando orden...');
        clearInterval(checkRestaurantInterval);
        this.loadOrderDetails(orderUuid);
      }
    }, 100); // Verificar cada 100ms

    // Timeout después de 10 segundos para evitar bucles infinitos
    setTimeout(() => {
      clearInterval(checkRestaurantInterval);
      const restaurantUuid = this.restaurantStore.selectedRestaurantUuid();

      if (!restaurantUuid || restaurantUuid.trim() === '') {
        console.error('❌ [PaymentsFormPageComponent] Timeout: No se pudo obtener restaurant UUID después de 10 segundos');
        this.router.navigate(['/auth/login']);
      }
    }, 10000);
  }

  /**
   * Llama al store para obtener la orden por UUID
   */
  private loadOrderDetails(orderUuid: string): void {
    console.log('🔄 [PaymentsFormPageComponent] Iniciando carga de orden con UUID:', orderUuid);
    console.log('🏪 [PaymentsFormPageComponent] Estado actual del store - loading:', this.loading(), 'error:', this.error(), 'currentOrder:', this.currentOrder());

    // Verificar si hay un restaurante seleccionado
    const restaurantUuid = this.restaurantStore.selectedRestaurantUuid();
    console.log('🏢 [PaymentsFormPageComponent] Restaurant UUID actual:', restaurantUuid);

    if (!restaurantUuid || restaurantUuid.trim() === '') {
      console.warn('⚠️ [PaymentsFormPageComponent] No hay restaurant UUID disponible, redirigiendo al login...');
      this.router.navigate(['/auth/login']);
      return;
    }

    this.orderStore.getOrderByUuid(orderUuid).subscribe({
      next: (order) => {
        console.log('✅ [PaymentsFormPageComponent] Orden recibida del backend:', order);
        console.log('🔄 [PaymentsFormPageComponent] Estado del store después - currentOrder:', this.currentOrder());
      },
      error: (error) => {
        console.error('❌ [PaymentsFormPageComponent] Error al cargar orden:', error);
        console.log('💥 [PaymentsFormPageComponent] Estado del store después del error - error:', this.error());

        // Si hay error de autenticación, redirigir al login
        if (error.message && error.message.includes('authentication')) {
          console.log('🔐 [PaymentsFormPageComponent] Error de autenticación detectado, redirigiendo al login...');
          this.router.navigate(['/auth/login']);
        }
      }
    });
  }

  ngOnDestroy() {
    console.log('🗑️ [PaymentsFormPageComponent] Componente destruido, limpiando estado de pagos');
    // Limpiar completamente el estado de pagos al destruir el componente
    this.paymentStore.clearPaymentState();
  }
}
