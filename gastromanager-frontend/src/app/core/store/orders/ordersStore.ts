import {patchState, signalStore, withHooks, withMethods, withState} from "@ngrx/signals";
import {inject} from "@angular/core";
import {StoreEventService} from "../../services/store-event/store-event.service";
import {Order, UninvoicedOrderItem} from "./order.model";
import {BaseStore} from "../base-store";
import {OrderService} from "../../services/order/order.service";
import {OrderRequestDto} from "../../../services/models/order-request-dto";
import {concatMap, Observable, tap} from "rxjs";
import {InvoiceRequestDto} from "../../model/interfaces/InvoiceRequestDto";

type OrderState = {
  orders: Order[]
  loading: boolean;
  error: string | null;
  uninvoicedOrderItems: UninvoicedOrderItem[];
}

const initialState: OrderState = {
  orders: [],
  loading: false,
  error: null,
  uninvoicedOrderItems: []
}

export const OrdersStore = signalStore(
  {providedIn: "root"},
  withState(initialState),

  withMethods((store, orderService = inject(OrderService), eventService = inject(StoreEventService)) => {
    const baseStore = new BaseStore(store, eventService);
    return {
      createOrder: (order: OrderRequestDto): Observable<Order[]> => {
        return baseStore.performOperation<Order[]>(
          orderService.createOrder(order).pipe(
            tap(uuid => console.log("UUID de la nueva orden:", uuid)),
            concatMap(() => orderService.getAllOrders()),
          ),
          (orders) => {
            console.log("Nuevas ordenes recuperadas:", orders);
            patchState(store, {orders: orders})
          },
          "Orden agregada con éxito"
        )
      },
      getOrderDetails:(orderUuid: string): Observable<Order> => {
        return baseStore.performOperation<Order>(
          orderService.getOrderByUuid(orderUuid).pipe(
            tap(order => console.log("Detalles de la orden obtenidos en tap", order)),
          ),
          (order) => {
            console.log("Nueva orden recuperada:", order);
          },
          "Orden obtenida con exito"
        )
      },
      loadOrders: (): Observable<Order[]> => {
        return baseStore.performOperation<Order[]>(
          orderService.getAllOrders(),
          (response) => {
            patchState(store, {orders: response})
          },
          "Ordenes cargadas con éxito"
        );
      },
      generateInvoices: (orderUuid: string, invoices: InvoiceRequestDto): Observable<Order[]> => {
        return baseStore.performOperation<Order[]>(
          orderService.generateInvoices(orderUuid, invoices).pipe(
            tap(uuids => console.log("UUIDs de los invoices", uuids)),
            concatMap(() => orderService.getAllOrders()),
          ),
          (orders) => {
            console.log("Nuevas ordenes recuperadas:", orders);
            patchState(store, {orders: orders})
          },
          "Invoices generados"
        )
      },
      getUninvoicedOrderItems: (orderUuid: string): Observable<UninvoicedOrderItem[]> => {
        return baseStore.performOperation<UninvoicedOrderItem[]>(
          orderService.getUninvoicedOrderItems(orderUuid),
          (response) => {
            // Se asume que el endpoint retorna un objeto con la propiedad "data"
            patchState(store, {uninvoicedOrderItems: response});
          },
          "Uninvoiced items cargados con éxito"
        );
      },
    }
  }),
  withHooks({
    onInit(store) {
      store.loadOrders();
    }
  })
);

