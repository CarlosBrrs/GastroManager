import {OrderItem} from "../cart/cart.model";
import {Invoice} from "../invoice/invoice.model";

export type Order = {
  uuid: string;
  code: string;
  user: string;
  customerNotes: string;
  orderItems: OrderItem[]
  totalPrice: number;
  operationalStatus: string;
  invoicingStatus: string;
  createdBy: string;
  createdDate: string;
  updatedBy: string;
  updatedDate: string;
  invoices: Invoice[]
}

export type UninvoicedOrderItem = {
  uuid: string;
  productItemUuid: string;
  productItemName: string;
  quantity: number;
  unitPrice: number;
}
