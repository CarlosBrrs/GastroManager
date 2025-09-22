import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Order } from '../../domain/models/order.interface';
import {OrdersAdapter} from "../../infrastructure/api/orders.adapter";
import {OrdersRepository} from "../../domain/ports/orders.repository";

@Injectable({
  providedIn: 'root'
})
export class GetOrderByUuidUseCase {
  private readonly ordersRepository: OrdersRepository = inject(OrdersAdapter);

  execute(orderUuid: string): Observable<Order> {
    return this.ordersRepository.getOrderByUuid(orderUuid);
  }
}
