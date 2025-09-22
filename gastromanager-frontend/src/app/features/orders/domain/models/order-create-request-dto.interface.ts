import {OrderItemRequestDto} from "../../../../services/models/order-item-request-dto";

export interface OrderCreateRequestDto {
  orderItems: OrderItemRequestDto[];
  customerNotes?: string;
  tableNumber?: string;
  paymentType?: PaymentType;
  customerName?: string;
}

export enum PaymentType {
  CASH = 'CASH',
  CARD = 'CARD',
  TRANSFER = 'TRANSFER'
}
