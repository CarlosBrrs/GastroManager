import {inject, Injectable} from '@angular/core';
import {catchError, Observable} from 'rxjs';
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {Page} from "../../../../core/model/interfaces/pagination/page.interface";
import {ProductsRepository} from "../../domain/ports/products.repository";
import {Product} from "../../domain/models/product.interface";
import {ApiGenericResponse} from "../../../../core/model/interfaces/ApiGenericResponse";
import {map} from "rxjs/operators";
import {mapToProduct, mapToProductRequestDto, mapToProductsByCategory} from "../../application/mappers/product.mapper";
import {ProductRequestDto} from "../../domain/models/product-request-dto.interface";
import {ProductSummaryResponseDto} from "../../domain/models/product-summary-response-dto.interface";
import {ProductGroupByResponseDto} from "../../domain/models/product-group-by-response-dto.interface";
import {ProductsByCategory} from "../../domain/models/products-by-category.interface";
import {environment} from "../../../../../environments/environment.dev";


@Injectable({
  providedIn: 'root'
})
export class ProductsAdapter implements ProductsRepository {

  private readonly http: HttpClient = inject(HttpClient);
  private readonly baseUrl: string = environment.API_URL;

  getAllProducts(params: { page: number, size: number }): Observable<Page<Product>> {
    const requestParams = new HttpParams()
      .set('page', params.page)
      .set('size', params.size);
    return this.http.get<ApiGenericResponse<Page<ProductSummaryResponseDto>>>(`${this.baseUrl}/products/search`,
      {
        params: requestParams
      }
    ).pipe(
      map((response: ApiGenericResponse<Page<ProductSummaryResponseDto>>) => {
        const mappedContent = response.data.content.map(dto => {
          return mapToProduct(dto)
        });
        return {
          ...response.data,
          content: mappedContent
        };
      }),
      catchError((error: HttpErrorResponse) => {
        throw new Error(error.error.message);
      })
    )
  }

  createProduct(product: Product): Observable<string> {
    const mappedProduct: ProductRequestDto = mapToProductRequestDto(product);
    return this.http.post<ApiGenericResponse<string>>(`${this.baseUrl}/products`, mappedProduct
    ).pipe(
      map((response: ApiGenericResponse<string>) => response.data),
      catchError((error: HttpErrorResponse) => {
        throw new Error(error.error.message);
      })
    );
  }

  getProductsGroupedByCategory(params?: {
    groupBy?: string;
    includeEmpty?: boolean;
    isEnabled?: boolean;
  }): Observable<ProductsByCategory> {
    let requestParams = new HttpParams();

    if (params?.groupBy) {
      requestParams = requestParams.set('groupBy', params.groupBy);
    } else {
      requestParams = requestParams.set('groupBy', 'category');
    }

    if (params?.includeEmpty !== undefined) {
      requestParams = requestParams.set('includeEmpty', params.includeEmpty.toString());
    } else {
      requestParams = requestParams.set('includeEmpty', 'false');
    }

    if (params?.isEnabled !== undefined) {
      requestParams = requestParams.set('isEnabled', params.isEnabled.toString());
    }

    return this.http.get<ApiGenericResponse<ProductGroupByResponseDto>>(`${this.baseUrl}/products/group-by`, {
      params: requestParams
    }).pipe(
      map((response: ApiGenericResponse<ProductGroupByResponseDto>) => {
        return mapToProductsByCategory(response.data);
      }),
      catchError((error: HttpErrorResponse) => {
        throw new Error(error.error.message);
      })
    );
  }

}
