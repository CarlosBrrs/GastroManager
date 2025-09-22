import {patchState, signalStore, withMethods, withState} from "@ngrx/signals";
import {inject} from "@angular/core";
import {catchError, finalize, tap, throwError} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";
import {ProcessPaymentUseCase} from "../../../features/payments/application/usecases/process-payment.use-case";
import {PaymentCreateData} from "../../../features/payments/domain/models/payment-create-data.interface";
import {OrderStore} from "../order/order.store";

type PaymentState = {
  loading: boolean;
  error: string | null;
  lastPaymentResponseUuid: string | null;
}

const initialState: PaymentState = {
  loading: false,
  error: null,
  lastPaymentResponseUuid: null
}

export const PaymentStore = signalStore(
  {providedIn: "root"},
  withState(initialState),
  withMethods((store, processPaymentUseCase = inject(ProcessPaymentUseCase),
               orderStore = inject(OrderStore)) => ({

    processPayment: (paymentData: PaymentCreateData) => {

      patchState(store, {loading: true, error: null, lastPaymentResponseUuid: null});

      return processPaymentUseCase.execute(paymentData).pipe(
        tap((response) => {
          console.log('✅ [PaymentStore] Pago procesado exitosamente:', response);
          patchState(store, {lastPaymentResponseUuid: response});

          // Forzar la actualización de órdenes después del pago
          orderStore.forceRefreshOrders({page: 0, size: 7});
        }),
        catchError((error: HttpErrorResponse) => {
          console.error('❌ [PaymentStore] Error al procesar pago:', error);
          const message = error.message || 'Error desconocido al procesar el pago';
          patchState(store, {error: message});
          return throwError(() => error);
        }),
        finalize(() => {
          console.log('🔄 [PaymentStore] Finalizando procesamiento de pago');
          patchState(store, {loading: false});
        })
      );
    },

    // Método para limpiar el estado después de un pago exitoso
    clearPaymentState: () => {
      console.log('🧹 [PaymentStore] Limpiando estado del pago');
      patchState(store, {
        error: null,
        lastPaymentResponseUuid: null,
        loading: false
      });
    },

    // Método para limpiar solo errores
    clearError: () => {
      console.log('🧹 [PaymentStore] Limpiando errores');
      patchState(store, {error: null});
    }

  }))
);
