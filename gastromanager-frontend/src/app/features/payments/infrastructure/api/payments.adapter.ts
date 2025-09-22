import { Injectable, inject } from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import { Observable, map, catchError, throwError } from 'rxjs';
import { PaymentRepository } from '../../domain/repositories/payment.repository';
import { PaymentCreateData } from '../../domain/models/payment-create-data.interface';
import { PaymentResponse } from '../../domain/models/payment-response.interface';
import { environment } from '../../../../../environments/environment';
import {ApiGenericResponse} from "../../../../core/model/interfaces/ApiGenericResponse";

// Interface para la respuesta del backend (estructura estándar con data que contiene solo el UUID)
interface PaymentApiResponse {
  timestamp: string;
  flag: boolean;
  message: string;
  data: string; // Solo el UUID del payment
}

@Injectable({
  providedIn: 'root'
})
export class PaymentsAdapter implements PaymentRepository {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = 'http://localhost:8080/api/v1/payments';

  processPayment(paymentData: PaymentCreateData): Observable<string> {
    console.log('🚀 [PaymentsAdapter] Enviando pago al backend:', paymentData);
    console.log('🌐 [PaymentsAdapter] URL del endpoint:', `${this.baseUrl}`);

    return this.http.post<ApiGenericResponse<string>>(this.baseUrl, paymentData).pipe(
      map((response: ApiGenericResponse<string>) => {
        console.log('📊 [PaymentsAdapter] UUID del pago procesado:', response.data);
        return response.data;
      }),
      catchError((error: HttpErrorResponse) => {
        console.error('❌ [PaymentsAdapter] Error en processPayment:', error);
        console.error('❌ [PaymentsAdapter] Status:', error.status);
        console.error('❌ [PaymentsAdapter] Error body:', error.error);

        let errorMessage = 'Error desconocido al procesar el pago';

        if (error.error?.message) {
          errorMessage = error.error.message;
        } else if (error.message) {
          errorMessage = error.message;
        } else if (error.status === 0) {
          errorMessage = 'No se pudo conectar con el servidor';
        } else if (error.status >= 500) {
          errorMessage = 'Error interno del servidor';
        } else if (error.status === 401) {
          errorMessage = 'No autorizado para procesar pagos';
        } else if (error.status === 403) {
          errorMessage = 'Acceso denegado para procesar pagos';
        } else if (error.status === 400) {
          errorMessage = 'Datos del pago inválidos';
        }

        console.error('❌ [PaymentsAdapter] Error final procesado:', errorMessage);
        return throwError(() => new Error(errorMessage));
      })
    );
  }
}
