export interface OrderDetailResponseDto {
  uuid: string;
  code: string;
  createdBy: string;
  customerName: string;
  customerNotes: string;
  tableNumber: string;
  totalAmount: number;
  totalPaid: number;
  remainingToPay: number;
  operationalStatus: string;
  paymentStatus: string;
  invoicingStatus: string;
  orderItems: OrderItemDetailDto[];
  updatedDate: string;
  invoices: any[];
  requiresPaymentBefore: boolean;
}

export interface OrderItemDetailDto {
  uuid: string;
  productUuid: string;
  productName: string;
  quantity: number;
  unitPrice: number;
  subtotal: number;
  customerNotes: string;
}
