import {inject, Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {PaymentRepository} from '../../domain/repositories/payment.repository';
import {PaymentCreateData} from '../../domain/models/payment-create-data.interface';
import {PaymentsAdapter} from "../../infrastructure/api/payments.adapter";

@Injectable({
  providedIn: 'root'
})
export class ProcessPaymentUseCase {
  private readonly paymentRepository: PaymentRepository = inject(PaymentsAdapter);

  execute(paymentData: PaymentCreateData): Observable<string> {
    console.log('💳 [ProcessPaymentUseCase] Ejecutando procesamiento de pago:', paymentData);
    return this.paymentRepository.processPayment(paymentData);
  }
}
