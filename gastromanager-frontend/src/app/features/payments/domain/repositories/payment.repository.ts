import { Observable } from 'rxjs';
import { PaymentCreateData } from '../models/payment-create-data.interface';
import { PaymentResponse } from '../models/payment-response.interface';

export abstract class PaymentRepository {
  abstract processPayment(paymentData: PaymentCreateData): Observable<string>;
}
