import {Observable} from "rxjs";
import {Page} from "../../../../core/model/interfaces/pagination/page.interface";
import {Order} from "../models/order.interface";

export interface OrdersRepository {

  getAllOrders(params: { page: number, size: number }): Observable<Page<Order>>;

  createOrder(order: Order): Observable<string>;

  getOrderByUuid(orderUuid: string): Observable<Order>;

  /*  createMenu(menu: Menu): Observable<string>;

    getMenuById(uuid: string): Observable<Menu>;

    editMenu(uuid: string, menu: Menu): Observable<Menu>;*/
}
