import {inject, Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {ProductsAdapter} from "../../infrastructure/api/products.adapter";
import {ProductsRepository} from "../../domain/ports/products.repository";
import {ProductsByCategory} from "../../domain/models/products-by-category.interface";

@Injectable({
  providedIn: 'root'
})
export class GetProductsGroupedByCategoryUseCase {

  private readonly productsRepo: ProductsRepository = inject(ProductsAdapter);

  execute(params?: {
    groupBy?: string;
    includeEmpty?: boolean;
    isEnabled?: boolean;
  }): Observable<ProductsByCategory> {
    return this.productsRepo.getProductsGroupedByCategory(params);
  }
}
