import {inject, Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {OrdersRepository} from "../../domain/ports/orders.repository";
import {OrdersAdapter} from "../../infrastructure/api/orders.adapter";
import {ChangeOrderStatusRequestDto} from "../../domain/models/change-order-status-request-dto.interface";

@Injectable({
  providedIn: 'root'
})
export class ChangeOrderStatusUseCase {

  private readonly ordersRepo: OrdersRepository = inject(OrdersAdapter);

  execute(orderUuid: string, changeStatus: ChangeOrderStatusRequestDto): Observable<string> {
    return this.ordersRepo.changeOrderStatus(orderUuid, changeStatus);
  }
}

