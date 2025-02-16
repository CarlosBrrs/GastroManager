import {OrderItem} from "../cart/cart.model";

export type Order = {
  uuid: string;
  code: string;
  user: string;
  customerNotes: string;
  orderItems: OrderItem[]
  amount: number;
  status: string;
  createdBy: string;
  createdDate: string;
  updatedBy: string;
  updatedDate: string;
}
