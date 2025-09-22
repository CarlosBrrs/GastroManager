export interface OrderSummaryResponseDto {
  uuid: string;
  code: string;
  totalAmount: number;
  totalPaid: number;
  remainingToPay: number;
  paymentStatus: string;
  operationalStatus: string;
  orderItems: OrderItemResponseDto[];
}

export interface OrderItemResponseDto {
  uuid: string;
  productUuid: string;
  productName: string;
  quantity: number;
  unitPrice: number;
  subtotal: number;
  customerNotes: string;
}
