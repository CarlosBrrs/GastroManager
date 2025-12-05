import {ChangeDetectionStrategy, Component, computed, inject, signal} from '@angular/core';
import {CreateProductFormComponent} from "../create-product-form/create-product-form.component";
import {Product} from "../../../domain/models/product.interface";
import {ProductStore} from "../../../../../core/store/product/product.store";
import {Router} from "@angular/router";
import {InventoryStore} from "../../../../../core/store/inventory/inventory.store";
import {Recipe} from "../../../../management/domain/models/recipe.interface";

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

  // Recetas mockeadas - TODO: Obtener del RecipeStore cuando esté disponible el método
  availableRecipes = signal<Recipe[]>([
    {
      uuid: '1',
      name: 'Salsa Roja Base',
      description: 'Salsa de tomate clásica para pizzas',
      cost: 450.50,
      ingredients: [
        {ingredientUuid: 'ing-1', ingredientName: 'Tomate triturado', quantity: 500},
        {ingredientUuid: 'ing-2', ingredientName: 'Aceite de oliva', quantity: 30},
        {ingredientUuid: 'ing-3', ingredientName: 'Ajo', quantity: 10},
        {ingredientUuid: 'ing-4', ingredientName: 'Orégano', quantity: 5}
      ],
      baseRecipe: null,
      isEnabled: true,
      createdBy: 'admin',
      updatedBy: 'admin',
      createdDate: '2025-01-01',
      updatedDate: '2025-01-01'
    },
    {
      uuid: '2',
      name: 'Masa para Pizza',
      description: 'Masa tradicional italiana',
      cost: 320.75,
      ingredients: [
        {ingredientUuid: 'ing-5', ingredientName: 'Harina 000', quantity: 1000},
        {ingredientUuid: 'ing-6', ingredientName: 'Agua', quantity: 600},
        {ingredientUuid: 'ing-7', ingredientName: 'Levadura fresca', quantity: 25},
        {ingredientUuid: 'ing-8', ingredientName: 'Sal', quantity: 20},
        {ingredientUuid: 'ing-2', ingredientName: 'Aceite de oliva', quantity: 50}
      ],
      baseRecipe: null,
      isEnabled: true,
      createdBy: 'admin',
      updatedBy: 'admin',
      createdDate: '2025-01-01',
      updatedDate: '2025-01-01'
    },
    {
      uuid: '3',
      name: 'Bechamel',
      description: 'Salsa bechamel cremosa para pastas',
      cost: 280.00,
      ingredients: [
        {ingredientUuid: 'ing-9', ingredientName: 'Leche', quantity: 1000},
        {ingredientUuid: 'ing-10', ingredientName: 'Mantequilla', quantity: 100},
        {ingredientUuid: 'ing-11', ingredientName: 'Harina común', quantity: 100},
        {ingredientUuid: 'ing-12', ingredientName: 'Nuez moscada', quantity: 2}
      ],
      baseRecipe: null,
      isEnabled: true,
      createdBy: 'admin',
      updatedBy: 'admin',
      createdDate: '2025-01-01',
      updatedDate: '2025-01-01'
    }
  ]);
  private readonly productsStore = inject(ProductStore);
  readonly loading = this.productsStore.loading;
  readonly error = this.productsStore.error;
  private readonly inventoryStore = inject(InventoryStore);
  // Computed para obtener ingredientes del store
  availableIngredients = computed(() => this.inventoryStore.allIngredients());
  private readonly router = inject(Router);

  constructor() {
    // Cargar ingredientes al inicializar el componente
    this.inventoryStore.getAllIngredientsNoPagination();
  }

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
