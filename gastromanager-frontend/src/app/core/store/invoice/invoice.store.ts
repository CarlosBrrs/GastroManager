import {patchState, signalStore, withHooks, withMethods, withState} from "@ngrx/signals";
import {inject} from "@angular/core";
import {StoreEventService} from "../../services/store-event/store-event.service";
import {BaseStore} from "../base-store";
import {OrderService} from "../../services/order/order.service";
import {OrderRequestDto} from "../../../services/models/order-request-dto";
import {concatMap, Observable, tap} from "rxjs";
import {Invoice} from "./invoice.model";
import {InvoiceService} from "../../services/invoice/invoice.service";

type InvoiceState = {
  invoices: Invoice[]
  loading: boolean;
  error: string | null;
}

const initialState: InvoiceState = {
  invoices: [],
  loading: false,
  error: null,
}

export const InvoiceStore = signalStore(
  {providedIn: "root"},
  withState(initialState),

  withMethods((store, orderService = inject(InvoiceService), eventService = inject(StoreEventService)) => {
    const baseStore = new BaseStore(store, eventService);
    return {
/*      createOrder: (orders: OrderRequestDto): Observable<Order[]> => {
        return baseStore.performOperation<Order[]>(
          orderService.createOrder(orders).pipe(
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
      loadOrders: (): Observable<Order[]> => {
        return baseStore.performOperation<Order[]>(
          orderService.getAllOrders(),
          (response) => {
            patchState(store, {orders: response})
          },
          "Ordenes cargadas con éxito"
        );
      }*/
    }
  }),
  withHooks({
    onInit(store) {
      // store.loadOrders();
    }
  })
);

