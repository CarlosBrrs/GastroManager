import {inject, Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {OrdersRepository} from "../../domain/ports/orders.repository";
import {OrdersAdapter} from "../../infrastructure/api/orders.adapter";

@Injectable({
  providedIn: 'root'
})
export class GetOrderTicketUseCase {

  private readonly ordersRepo: OrdersRepository = inject(OrdersAdapter);

  execute(orderUuid: string): Observable<ArrayBuffer> {
    return this.ordersRepo.getOrderTicket(orderUuid);
  }
}

