import {Injectable} from '@angular/core';
import {BaseHttpService} from "../basehttp/base-http.service";
import {Observable} from "rxjs";
import {OrderRequestDto} from "../../../services/models/order-request-dto";
import {Order, UninvoicedOrderItem} from "../../store/orders/order.model";
import {InvoiceRequestDto} from "../../model/interfaces/InvoiceRequestDto";

@Injectable({
  providedIn: 'root'
})
export class OrderService extends BaseHttpService {

  createOrder(order: OrderRequestDto): Observable<string> {
    return this.handleRequest<string>("POST", "orders", order);
  }

  getAllOrders() {
    return this.handleRequest<Order[]>("GET", "orders");
  }

  getOrderByUuid(orderUuid: string): Observable<Order> {
    return this.handleRequest<Order>("GET", `orders/${orderUuid}`);
  }

  generateInvoices(orderUuid: string, invoices: InvoiceRequestDto) {
    return this.handleRequest<string[]>("POST", `orders/${orderUuid}/invoices`, invoices);
  }

  getUninvoicedOrderItems(orderUuid: string) {
    return this.handleRequest<UninvoicedOrderItem[]>("GET", `orders/${orderUuid}/uninvoiced-items`);
  }
}
