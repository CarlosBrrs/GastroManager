import {inject, Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {Page} from "../../../../core/model/interfaces/pagination/page.interface";
import {Order} from "../../domain/models/order.interface";
import {OrdersRepository} from "../../domain/ports/orders.repository";
import {OrdersAdapter} from "../../infrastructure/api/orders.adapter";

@Injectable({
  providedIn: 'root'
})
export class GetOrdersUseCase {

  private readonly ordersRepo: OrdersRepository = inject(OrdersAdapter);

  execute(params: { page: number, size: number }): Observable<Page<Order>> {
    return this.ordersRepo.getAllOrders(params);
  }
}
