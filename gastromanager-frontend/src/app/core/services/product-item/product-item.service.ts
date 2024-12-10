import {Injectable} from '@angular/core';
import {BaseHttpService} from "../basehttp/base-http.service";
import {Observable, tap} from "rxjs";
import {ApiGenericResponse} from "../../model/interfaces/ApiGenericResponse";
import {ProductItemResponseDto} from "../../model/interfaces/ProductItemResponseDto";
import {ProductItem} from "../../store/product-item/product-item.model";

@Injectable({
  providedIn: 'root'
})
export class ProductItemService extends BaseHttpService {


  getAllProductItemsTest(): Observable<ApiGenericResponse<ProductItem[]>> {
    return this.http.get<ApiGenericResponse<ProductItem[]>>(`${this.apiUrl}/product-items`, {headers: {'Accept': 'application/json'}})
      .pipe(
        tap(response => {
          if (response.flag) {
            console.log('Productos obtenidos:', response.data);
          }
        })
      );
  }

  getAllProductItems(): Observable<ApiGenericResponse<ProductItemResponseDto[]>> {
    return this.http.get<ApiGenericResponse<ProductItemResponseDto[]>>(`${this.apiUrl}/product-items`, {headers: {'Accept': 'application/json'}}).pipe(
      tap(response => {
        if (response.flag) {
          console.log('Productos obtenidos:', response.data);
        }
      })
    );
  }

  addProductItem(productItem: any): Observable<ApiGenericResponse<string>> {
    return this.http.post<ApiGenericResponse<string>>(`${this.apiUrl}/product-items`, productItem, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      }
    }).pipe(
      tap(response => {
        if (response.flag) {
          console.log('Producto añadido con UUID:', response.data);
        }
      })
    )
  }

  updateProductItem(uuid: string, payload: any): Observable<ApiGenericResponse<any>> {
    return this.http.put<ApiGenericResponse<string>>(`${this.apiUrl}/product-items/${uuid}`, payload, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      }
    }).pipe(
      tap(response => {
        if (response.flag) {
          console.log('Producto actualizado:', response.data);
        }
      })
    )
  }

  deleteProductItem(productItemUuid: string): void {
    alert("not implemented yet");
  }
}
