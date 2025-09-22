// Interface para los datos del pago que se enviarán al backend
export interface PaymentCreateData {
  amount: number;
  paymentMethod: string;
  tipAmount: number;
  notes?: string;
  orderUuid: string;
  transactionId?: string;
}

