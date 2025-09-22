import {Observable} from "rxjs";
import {Page} from "../../../../core/model/interfaces/pagination/page.interface";
import {Product} from "../models/product.interface";
import {ProductsByCategory} from "../models/products-by-category.interface";

export interface ProductsRepository {

  getAllProducts(params: { page: number, size: number }): Observable<Page<Product>>;

  createProduct(product: Product): Observable<string>;

  getProductsGroupedByCategory(params?: {
    groupBy?: string;
    includeEmpty?: boolean;
    isEnabled?: boolean;
  }): Observable<ProductsByCategory>;

}
