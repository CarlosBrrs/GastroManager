import {inject, Injectable} from "@angular/core";
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {catchError, Observable} from "rxjs";
import {Page} from "../../../../core/model/interfaces/pagination/page.interface";
import {OrdersRepository} from "../../domain/ports/orders.repository";
import {Order} from "../../domain/models/order.interface";
import {ApiGenericResponse} from "../../../../core/model/interfaces/ApiGenericResponse";
import {map} from "rxjs/operators";
import {OrderSummaryResponseDto} from "../../domain/models/order-summary-response-dto.interface";
import {OrderDetailResponseDto} from "../../domain/models/order-detail-response-dto.interface";
import {mapOrderDetailToOrder, mapToOrder, mapToOrderCreateRequestDto} from "../../application/mappers/order.mapper";
import {OrderCreateRequestDto} from "../../domain/models/order-create-request-dto.interface";
import {environment} from "../../../../../environments/environment";
import {ChangeOrderStatusRequestDto} from "../../domain/models/change-order-status-request-dto.interface";

@Injectable({
  providedIn: 'root'
})
export class OrdersAdapter implements OrdersRepository {

  private readonly http: HttpClient = inject(HttpClient);
  private readonly baseUrl: string = environment.API_URL;


  getAllOrders(params: { page: number; size: number; }): Observable<Page<Order>> {
    console.log('🔄 [OrdersAdapter] Iniciando llamado getAllOrders con parámetros:', params);

    const requestParams = new HttpParams()
      .set('page', params.page)
      .set('size', params.size);

    console.log('🔄 [OrdersAdapter] URL de petición:', `${this.baseUrl}/orders`);
    console.log('🔄 [OrdersAdapter] Parámetros de consulta:', requestParams.toString());

    return this.http.get<ApiGenericResponse<Page<OrderSummaryResponseDto>>>(`${this.baseUrl}/orders`,
      {
        params: requestParams
      }
    ).pipe(
      map((response: ApiGenericResponse<Page<OrderSummaryResponseDto>>) => {
        console.log('✅ [OrdersAdapter] Respuesta exitosa del backend:', response);
        console.log('✅ [OrdersAdapter] Número de órdenes recibidas:', response.data.content.length);
        console.log('✅ [OrdersAdapter] Total de elementos:', response.data.totalElements);

        const mappedContent = response.data.content.map(dto => {
          return mapToOrder(dto)
        });

        const mappedResponse = {
          ...response.data,
          content: mappedContent
        };

        console.log('✅ [OrdersAdapter] Respuesta mapeada:', mappedResponse);
        return mappedResponse;
      }),
      catchError((error: HttpErrorResponse) => {
        console.error('❌ [OrdersAdapter] Error en getAllOrders:', error);
        console.error('❌ [OrdersAdapter] Status:', error.status);
        console.error('❌ [OrdersAdapter] Error body:', error.error);
        throw new Error(error.error.message);
      })
    )
  }

  createOrder(order: Order): Observable<string> {
    console.log('🔄 [OrdersAdapter] Iniciando llamado createOrder con orden:', order);

    const mappedOrder: OrderCreateRequestDto = mapToOrderCreateRequestDto(order);
    console.log('🔄 [OrdersAdapter] Orden mapeada a DTO:', mappedOrder);
    console.log('🔄 [OrdersAdapter] URL de petición:', `${this.baseUrl}/orders`);
    console.log('🔄 [OrdersAdapter] Método: POST');

    return this.http.post<ApiGenericResponse<string>>(`${this.baseUrl}/orders`, mappedOrder
    ).pipe(
      map((response: ApiGenericResponse<string>) => {
        console.log('✅ [OrdersAdapter] Respuesta exitosa de createOrder:', response);
        console.log('✅ [OrdersAdapter] UUID de orden creada:', response.data);
        return response.data;
      }),
      catchError((error: HttpErrorResponse) => {
        console.error('❌ [OrdersAdapter] Error en createOrder:', error);
        console.error('❌ [OrdersAdapter] Status:', error.status);
        console.error('❌ [OrdersAdapter] Error body:', error.error);
        console.error('❌ [OrdersAdapter] Orden que falló:', order);
        throw new Error(error.error.message);
      })
    )
  }

  getOrderByUuid(orderUuid: string): Observable<Order> {
    console.log('🔄 [OrdersAdapter] Iniciando llamado getOrderByUuid con UUID:', orderUuid);
    console.log('🔄 [OrdersAdapter] URL de petición:', `${this.baseUrl}/orders/${orderUuid}`);

    return this.http.get<ApiGenericResponse<OrderDetailResponseDto>>(`${this.baseUrl}/orders/${orderUuid}`
    ).pipe(
      map((response: ApiGenericResponse<OrderDetailResponseDto>) => {
        console.log('✅ [OrdersAdapter] Respuesta exitosa de getOrderByUuid:', response);
        const mappedOrder = mapOrderDetailToOrder(response.data);
        console.log('✅ [OrdersAdapter] Orden mapeada:', mappedOrder);
        return mappedOrder;
      }),
      catchError((error: HttpErrorResponse) => {
        console.error('❌ [OrdersAdapter] Error en getOrderByUuid:', error);
        console.error('❌ [OrdersAdapter] Status:', error.status);
        console.error('❌ [OrdersAdapter] Error body:', error.error);
        console.error('❌ [OrdersAdapter] UUID que falló:', orderUuid);
        throw new Error(error.error.message);
      })
    )
  }

  changeOrderStatus(orderUuid: string, changeStatus: ChangeOrderStatusRequestDto): Observable<string> {
    console.log('🔄 [OrdersAdapter] Iniciando llamado changeOrderStatus con UUID:', orderUuid);
    console.log('🔄 [OrdersAdapter] Cambio de estado:', changeStatus);
    console.log('🔄 [OrdersAdapter] URL de petición:', `${this.baseUrl}/orders/${orderUuid}/status`);

    return this.http.patch<ApiGenericResponse<string>>(`${this.baseUrl}/orders/${orderUuid}/status`, changeStatus
    ).pipe(
      map((response: ApiGenericResponse<string>) => {
        console.log('✅ [OrdersAdapter] Respuesta exitosa de changeOrderStatus:', response);
        console.log('✅ [OrdersAdapter] UUID de orden actualizada:', response.data);
        return response.data;
      }),
      catchError((error: HttpErrorResponse) => {
        console.error('❌ [OrdersAdapter] Error en changeOrderStatus:', error);
        console.error('❌ [OrdersAdapter] Status:', error.status);
        console.error('❌ [OrdersAdapter] Error body:', error.error);
        console.error('❌ [OrdersAdapter] UUID que falló:', orderUuid);
        throw new Error(error.error.message);
      })
    )
  }

  getOrderTicket(orderUuid: string): Observable<ArrayBuffer> {
    console.log('🔄 [OrdersAdapter] Iniciando llamado getOrderTicket con UUID:', orderUuid);
    console.log('🔄 [OrdersAdapter] URL de petición:', `${this.baseUrl}/orders/${orderUuid}/ticket`);

    return this.http.get(`${this.baseUrl}/orders/${orderUuid}/ticket`, {
      responseType: 'arraybuffer'
    }).pipe(
      map((response: ArrayBuffer) => {
        console.log('✅ [OrdersAdapter] Respuesta exitosa de getOrderTicket');
        console.log('✅ [OrdersAdapter] PDF bytes recibidos (longitud):', response.byteLength);
        return response;
      }),
      catchError((error: HttpErrorResponse) => {
        console.error('❌ [OrdersAdapter] Error en getOrderTicket:', error);
        console.error('❌ [OrdersAdapter] Status:', error.status);
        console.error('❌ [OrdersAdapter] Error body:', error.error);
        console.error('❌ [OrdersAdapter] UUID que falló:', orderUuid);
        throw new Error(error.error?.message || 'Error al obtener el ticket');
      })
    )
  }

}
