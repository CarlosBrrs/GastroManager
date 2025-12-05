import {inject, Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {Order} from "../../domain/models/order.interface";
import {OrdersRepository} from "../../domain/ports/orders.repository";
import {OrdersAdapter} from "../../infrastructure/api/orders.adapter";

@Injectable({
  providedIn: 'root'
})
export class CreateOrderUseCase {

  private readonly ordersRepo: OrdersRepository = inject(OrdersAdapter);

  execute(order: Order): Observable<string> {
    return this.ordersRepo.createOrder(order);
  }
}
