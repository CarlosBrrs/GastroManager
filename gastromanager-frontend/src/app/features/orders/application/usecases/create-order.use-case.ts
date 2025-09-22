import {inject, Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {Page} from "../../../../core/model/interfaces/pagination/page.interface";
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
