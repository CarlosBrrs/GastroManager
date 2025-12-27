import {Observable} from "rxjs";
import {Page} from "../../../../core/model/interfaces/pagination/page.interface";
import {Order} from "../models/order.interface";
import {ChangeOrderStatusRequestDto} from "../models/change-order-status-request-dto.interface";

export interface OrdersRepository {

  getAllOrders(params: { page: number, size: number }): Observable<Page<Order>>;

  createOrder(order: Order): Observable<string>;

  getOrderByUuid(orderUuid: string): Observable<Order>;

  changeOrderStatus(orderUuid: string, changeStatus: ChangeOrderStatusRequestDto): Observable<string>;

  getOrderTicket(orderUuid: string): Observable<ArrayBuffer>;

  /*  createMenu(menu: Menu): Observable<string>;

    getMenuById(uuid: string): Observable<Menu>;

    editMenu(uuid: string, menu: Menu): Observable<Menu>;*/
}
