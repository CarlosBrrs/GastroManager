import {Injectable} from '@angular/core';
import {BaseHttpService} from "../basehttp/base-http.service";
import {Observable, tap} from "rxjs";
import {ApiGenericResponse} from "../../model/interfaces/ApiGenericResponse";
import {ProductItemResponseDto} from "../../model/interfaces/ProductItemResponseDto";
import {ProductItem} from "../../store/product-item/product-item.model";
import {ProductItemRequestDto} from "../../model/interfaces/ProductItemRequestDto";

@Injectable({
  providedIn: 'root'
})
export class ProductItemService extends BaseHttpService {

  getAllProductItems(): Observable<ProductItem[]> {
    return this.handleRequest<ProductItem[]>("GET", "product-items");
  }


  getAllProductItemsToDelete(): Observable<ApiGenericResponse<ProductItemResponseDto[]>> {
    return this.http.get<ApiGenericResponse<ProductItemResponseDto[]>>(`${this.apiUrl}/product-items`, {headers: {'Accept': 'application/json'}}).pipe(
      tap(response => {
        if (response.flag) {
          console.log('Productos obtenidos:', response.data);
        }
      })
    );
  }

  addProductItem(productItem: ProductItemRequestDto): Observable<string> {
    return this.handleRequest<string>("POST", "product-items", productItem);
  }

  updateProductItem(uuid: string, payload: ProductItemRequestDto): Observable<ProductItem> {
    return this.handleRequest<ProductItem>("PUT", `product-items/${uuid}`, payload);
  }

  deleteProductItem(productItemUuid: string): void {
    alert("not implemented yet");
  }
}
