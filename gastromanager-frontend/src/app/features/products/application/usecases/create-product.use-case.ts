import {inject, Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {ProductsAdapter} from "../../infrastructure/api/products.adapter";
import {Page} from "../../../../core/model/interfaces/pagination/page.interface";
import {Product} from "../../domain/models/product.interface";
import {ProductsRepository} from "../../domain/ports/products.repository";

@Injectable({
  providedIn: 'root'
})
export class CreateProductUseCase {

  private readonly productsRepo: ProductsRepository = inject(ProductsAdapter);

  execute(product: Product): Observable<string> {
    return this.productsRepo.createProduct(product);
  }
}
