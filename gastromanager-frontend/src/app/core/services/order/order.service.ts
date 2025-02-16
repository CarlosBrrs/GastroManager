import { Injectable } from '@angular/core';
import {BaseHttpService} from "../basehttp/base-http.service";
import {IngredientRequestDto} from "../../model/interfaces/IngredientRequestDto";
import {Observable, tap} from "rxjs";
import {ApiGenericResponse} from "../../model/interfaces/ApiGenericResponse";
import {OrderRequestDto} from "../../../services/models/order-request-dto";
import {IngredientResponseDto} from "../../model/interfaces/IngredientResponseDto";
import {ProductItem} from "../../store/product-item/product-item.model";
import {Order} from "../../store/order/order.model";

@Injectable({
  providedIn: 'root'
})
export class OrderService extends BaseHttpService {

  createOrder(order: OrderRequestDto): Observable<ApiGenericResponse<string>> {
    return this.http.post<ApiGenericResponse<string>>(`${this.apiUrl}/orders`, order, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      }
    }).pipe(
      tap(response => {
        if (response.flag) {
          console.log('Orden colocada con UUID:', response.data);
        }
      })
    )

  }

  getAllOrders() {
    return this.handleRequest<Order[]>("GET", "orders");
  }
}
