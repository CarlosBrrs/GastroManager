import {patchState, signalStore, withHooks, withMethods, withState} from "@ngrx/signals";
import {effect, inject} from "@angular/core";
import {ColumnProperties} from "../inventory/inventory.store";
import {rxMethod} from "@ngrx/signals/rxjs-interop";
import {paginationParams} from "../../model/interfaces/pagination/pagination-params.interface";
import {catchError, concatMap, finalize, map, of, pipe, switchMap, tap, throwError} from "rxjs";
import {tapResponse} from "@ngrx/operators";
import {HttpErrorResponse} from "@angular/common/http";
import {Order} from "../../../features/orders/domain/models/order.interface";
import {GetOrdersUseCase} from "../../../features/orders/application/usecases/get-orders.use-case";
import {CreateOrderUseCase} from "../../../features/orders/application/usecases/create-order.use-case";
import {GetOrderByUuidUseCase} from "../../../features/orders/application/usecases/get-order-by-uuid.use-case";
import {RestaurantStore} from "../restaurant/restaurant.store";
import {ChangeOrderStatusUseCase} from "../../../features/orders/application/usecases/change-order-status.use-case";
import {GetOrderTicketUseCase} from "../../../features/orders/application/usecases/get-order-ticket.use-case";
import type {ChangeOrderStatusRequestDto} from "../../../features/orders/domain/models/change-order-status-request-dto.interface";

type OrderState = {
  pages: Map<number, Order[]>;
  currentPage: number;
  totalRecords: number;
  tableColumns: Array<ColumnProperties>;
  loading: boolean;
  error: string | null;
  currentOrder: Order | null; // Estado para la orden actual
  // orderToEdit: Order | null;
  selectedOrder: Order | null;
}

const initialState: OrderState = {
  pages: new Map<number, Order[]>(),
  currentPage: 0,
  totalRecords: 0,
  tableColumns: [
    {
      field: 'code',
      header: 'Código',
      prefix: '📋 ',
      suffix: ' ✓'
    },
    {
      field: 'orderItems',
      header: 'Items de orden',
      pipe: 'orderItems',
      // prefix: '<div style="background-color: #f0f8ff; padding: 8px; border-radius: 4px; border-left: 4px solid #007bff;"><strong>Productos ordenados:</strong><br>',
      suffix: '</div>'
    },
    {
      field: 'totalAmount',
      header: 'Total',
      pipe: 'currency',
      prefix: '💰 <span style="color: green; font-weight: bold;">',
      suffix: ' COP</span>'
    },
    // Nueva columna: restante por pagar
    {
      field: 'remainingToPay',
      header: 'Restante por pagar',
      pipe: 'currency',
      prefix: '<span style="color: #b21f1f; font-weight: 600;">',
      suffix: ' COP</span>'
    },
    // Nueva columna: estado del pago
    {
      field: 'paymentStatus',
      header: 'Estado de Pago',
      pipe: 'orderStatus'
    },
    /*        {field: 'category', header: 'Categoría'},
            {field: 'createdBy', header: 'Creado Por'},
            {field: 'createdDate', header: 'Fecha de Creación'},
            {field: 'updatedBy', header: 'Actualizado Por'},
            {field: 'updatedDate', header: 'Fecha de Actualización'}*/
  ],
  loading: false,
  error: null,
  currentOrder: null,
  selectedOrder: null,
}

export const OrderStore = signalStore(
  {providedIn: "root",},
  withState(initialState),
  withMethods((store,
               getOrders = inject(GetOrdersUseCase),
               createOrder = inject(CreateOrderUseCase),
               getOrderByUuid = inject(GetOrderByUuidUseCase),
               changeOrderStatus = inject(ChangeOrderStatusUseCase),
               getOrderTicket = inject(GetOrderTicketUseCase)
               /* createOrder = inject(CreateOrderUseCase),
                editIngredient = inject(EditIngredientUseCase),
                getIngredientByUuid = inject(GetIngredientByUuidUseCase)*/) => ({
      getOrders: rxMethod<paginationParams>(
        pipe(
          tap(() => {
            patchState(store, {loading: true, error: null});
          }),
          switchMap((params) => {

              const page = params.page;
              if (store.pages().has(page)) {
                patchState(store, {currentPage: page, loading: false, error: null});
                return of(store.pages().get(page));
              }
              return getOrders.execute(params).pipe(
                tapResponse({
                  next: (response) => {
                    console.log("Respuesta de getOrders:", response);
                    const {number, content} = response;
                    const updatedPages = new Map(store.pages());
                    updatedPages.set(number, content);
                    patchState(store, {
                      pages: updatedPages,
                      currentPage: page,
                      totalRecords: response.totalElements
                    });
                  },
                  error: (error: HttpErrorResponse) => {
                    const message = error.message || 'Error desconocido';
                    patchState(store, {error: message});
                  },
                  finalize: () => {
                    patchState(store, {loading: false})
                  }
                })
              )
            }
          )
        )),
      createOrder: (order: Order) => {
        patchState(store, {loading: true, error: null});
        return createOrder.execute(order).pipe(
          tap(createdOrderUuid => {
            console.log('Order created with uuid:', createdOrderUuid);
          }),
          catchError((error: HttpErrorResponse) => {
            const message = error.message || 'Error desconocido';
            patchState(store, {error: message});
            return throwError(() => error);
          }),
          concatMap(createdOrderUuid => {
            console.log('New Order with UUID added to the list, reloading...:', createdOrderUuid);
            let params: paginationParams = {page: 0, size: 7};
            return getOrders.execute(params).pipe(
              tapResponse({
                next: response => {
                  console.log("Respuesta de createOrder:", response);
                  const {number, content} = response;
                  const updatedPages = new Map(store.pages());
                  updatedPages.set(number, content);
                  patchState(store, {
                    pages: updatedPages,
                    currentPage: params.page,
                    totalRecords: response.totalElements
                  });
                },
                error: (error: HttpErrorResponse) => {
                  const message = error.message || 'Error desconocido';
                  patchState(store, {error: message});
                },
              }),
              map(() => createdOrderUuid)
            )
          }),
          finalize(() => {
            patchState(store, {loading: false});
          })
        );
      },

      getOrderByUuid: (orderUuid: string) => {
        patchState(store, {loading: true, error: null});
        return getOrderByUuid.execute(orderUuid).pipe(
          tap(order => {
            console.log('📦 Order retrieved by UUID:', order);
            // Guardar la orden en el estado currentOrder
            patchState(store, {currentOrder: order});
          }),
          catchError((error: HttpErrorResponse) => {
            const message = error.message || 'Error desconocido';
            patchState(store, {error: message});
            return throwError(() => error);
          }),
          finalize(() => {
            patchState(store, {loading: false});
          })
        );
      },
      forceRefreshOrders: rxMethod<paginationParams>(
        pipe(
          tap(() => {
            patchState(store, {loading: true, error: null});
          }),
          switchMap((params) => {
            const page = params.page;
            return getOrders.execute(params).pipe(
              tapResponse({
                next: (response) => {
                  console.log("🔄 [OrderStore] Órdenes actualizadas forzosamente:", response);
                  const {number, content} = response;
                  const refreshedPages = new Map<number, Order[]>();
                  refreshedPages.set(number, content);
                  patchState(store, {
                    pages: refreshedPages,
                    currentPage: page,
                    totalRecords: response.totalElements
                  });
                },
                error: (error: HttpErrorResponse) => {
                  const message = error.message || 'Error desconocido';
                  patchState(store, {error: message});
                },
                finalize: () => {
                  patchState(store, {loading: false});
                }
              })
            );
          })
        )
      ),
      changeStatus: (orderUuid: string, changeStatus: ChangeOrderStatusRequestDto) => {
        patchState(store, {loading: true, error: null});

        return changeOrderStatus.execute(orderUuid, changeStatus).pipe(
          tap(updatedOrderUuid => {
            console.log('✅ [OrderStore] Estado de orden cambiado exitosamente:', updatedOrderUuid);
          }),
          concatMap(updatedOrderUuid => {
            console.log('📄 [OrderStore] Obteniendo ticket PDF para orden:', updatedOrderUuid);
            return getOrderTicket.execute(orderUuid).pipe(
              tap(pdfArrayBuffer => {
                console.log('✅ [OrderStore] Ticket PDF obtenido exitosamente');
                console.log('📄 [OrderStore] Longitud PDF bytes:', pdfArrayBuffer.byteLength);
              }),
              catchError((error: HttpErrorResponse) => {
                const message = error.message || 'Error desconocido al obtener ticket';
                console.error('❌ [OrderStore] Error al obtener ticket:', message);
                patchState(store, {error: message});

                // Lanzar error personalizado para que el componente lo maneje
                return throwError(() => ({
                  type: 'TICKET_ERROR',
                  message: 'El estado fue actualizado pero no se pudo generar el ticket PDF',
                  originalError: error
                }));
              }),
              map((pdfArrayBuffer) => ({orderUuid: updatedOrderUuid, pdfData: pdfArrayBuffer}))
            );
          }),
          concatMap((result) => {
            console.log('🔄 [OrderStore] Refrescando lista de órdenes después del cambio de estado');
            let params: paginationParams = {page: store.currentPage(), size: 7};
            return getOrders.execute(params).pipe(
              tap(response => {
                console.log("✅ [OrderStore] Lista de órdenes actualizada:", response);
                const {number, content} = response;
                const updatedPages = new Map(store.pages());
                updatedPages.set(number, content);
                patchState(store, {
                  pages: updatedPages,
                  currentPage: params.page,
                  totalRecords: response.totalElements
                });
              }),
              catchError((error: HttpErrorResponse) => {
                const message = error.message || 'Error desconocido al refrescar órdenes';
                console.error('❌ [OrderStore] Error al refrescar órdenes:', message);
                patchState(store, {error: message});
                // Continuar el flujo aunque falle el refresh
                return of(null);
              }),
              map(() => result)
            );
          }),
          catchError((error: any) => {
            const message = error?.message || error?.error?.message || 'Error desconocido al cambiar estado';
            console.error('❌ [OrderStore] Error en changeStatus:', message);
            patchState(store, {error: message});

            // Re-lanzar el error para que el componente lo maneje
            return throwError(() => error);
          }),
          finalize(() => {
            // SIEMPRE ejecutar esto, incluso si hay error
            patchState(store, {loading: false});
            console.log('🏁 [OrderStore] Finalizando changeStatus, loading = false');
          })
        );
      }
    }),
  ),
  withHooks(
    {
      onInit: (store) => {
        const restaurantStore = inject(RestaurantStore);

        // Effect que reacciona cuando cambia el restaurante seleccionado
        // Solo en la inicialización, no durante navegación de páginas
        effect(() => {
          const restaurantUuid = restaurantStore.selectedRestaurantUuid();
          console.log('🔄 [OrderStore] Restaurant UUID changed:', restaurantUuid);

          // Solo cargar órdenes si tenemos un restaurante válido
          // Y solo si no hay páginas cargadas (inicialización)
          if (restaurantUuid && restaurantUuid.trim() !== '' && store.pages().size === 0) {
            console.log('✅ [OrderStore] Loading orders for restaurant (initial load):', restaurantUuid);
            store.getOrders({page: 0, size: 7});
          } else if (!restaurantUuid || restaurantUuid.trim() === '') {
            console.log('⚠️ [OrderStore] No restaurant UUID available, skipping orders load');
          } else {
            console.log('🔄 [OrderStore] Restaurant changed but pages already loaded, skipping auto-load');
          }
        }, {allowSignalWrites: true});
      }
    }
  )
);
