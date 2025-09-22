import {ChangeDetectionStrategy, Component, inject} from '@angular/core';
import {CreateProductFormComponent} from "../create-product-form/create-product-form.component";
import {Product} from "../../../domain/models/product.interface";
import {ProductStore} from "../../../../../core/store/product/product.store";
import {Router} from "@angular/router";

@Component({
  selector: 'gm-create-update-product-page',
  standalone: true,
  imports: [
    CreateProductFormComponent
  ],
  templateUrl: './create-update-products-page.component.html',
  styleUrl: './create-update-products-page.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CreateUpdateProductsPageComponent {

  private readonly productsStore = inject(ProductStore);
  private readonly router = inject(Router);

  readonly loading = this.productsStore.loading;
  readonly error = this.productsStore.error;

  createProductHandler($event: Product) {
    this.productsStore.createProduct($event).subscribe({
      next: () => {
        this.router.navigate(['/products']);
      },
      error: (error) => {
        console.error('Error creating product:', error);
      }
    });
  }
}
