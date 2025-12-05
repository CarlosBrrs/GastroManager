import {ChangeDetectionStrategy, Component, EventEmitter, inject, input, Output, signal} from '@angular/core';
import {InputNumberModule} from "primeng/inputnumber";
import {InputTextModule} from "primeng/inputtext";
import {PaginatorModule} from "primeng/paginator";
import {RadioButtonModule} from "primeng/radiobutton";
import {FormControl, FormGroup, NonNullableFormBuilder, ReactiveFormsModule, Validators} from "@angular/forms";
import {ProgressSpinnerModule} from "primeng/progressspinner";
import {Product} from "../../../domain/models/product.interface";
import {CommonModule} from "@angular/common";
import {Ingredient} from "../../../../ingredients/domain/models/ingredient.interface";
import {Recipe} from "../../../../management/domain/models/recipe.interface";
import {InputSwitchModule} from "primeng/inputswitch";
import {SelectedIngredient} from "../../../domain/models/selected-ingredient.interface";
import {SelectedRecipe} from "../../../domain/models/selected-recipe.interface";

@Component({
  selector: 'gm-create-product-form',
  standalone: true,
  imports: [
    CommonModule,
    InputNumberModule,
    InputTextModule,
    PaginatorModule,
    RadioButtonModule,
    ReactiveFormsModule,
    ProgressSpinnerModule,
    InputSwitchModule
  ],
  templateUrl: './create-product-form.component.html',
  styleUrl: './create-product-form.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CreateProductFormComponent {

  fb = inject(NonNullableFormBuilder);

  // Modo de creación: 'basic' o 'advanced'
  creationMode = signal<'basic' | 'advanced'>('basic');

  // Ingredientes y recetas para modo avanzado
  selectedIngredients = signal<SelectedIngredient[]>([]);
  selectedRecipes = signal<SelectedRecipe[]>([]);
  ingredientSearchTerm = signal<string>('');
  recipeSearchTerm = signal<string>('');

  // Inputs para recibir datos del padre (para modo avanzado)
  availableIngredients = input<Ingredient[]>([]);
  availableRecipes = input<Recipe[]>([]);

  productForm: FormGroup = this.fb.group({
    uuid: this.fb.control('', []),
    name: this.fb.control("", [Validators.required, Validators.minLength(3)]),
    description: this.fb.control("", [Validators.required, Validators.minLength(3)]),
    category: this.fb.control("", [Validators.required, Validators.minLength(3)]),
    purchasePrice: new FormControl<number>(0, [Validators.required, Validators.min(0.00001)]),
    salePrice: new FormControl<number>(0, [Validators.required, Validators.min(0.00001)]),
  });

  loading = input<boolean>(false);
  error = input<string | null>(null);
  @Output() onCreateProduct = new EventEmitter<Product>();

  // Computed para calcular el precio de compra en modo avanzado
  get calculatedPurchasePrice(): number {
    // Costo de los ingredientes seleccionados
    const ingredientsCost = this.selectedIngredients().reduce((total, ingredient) => {
      const cost = ingredient.quantity * ingredient.pricePerUnit;
      return total + cost;
    }, 0);

    // Sumar el costo de las recetas seleccionadas con su multiplicador
    const recipesCost = this.selectedRecipes().reduce((total, recipe) => {
      const cost = recipe.cost * recipe.multiplier;
      return total + cost;
    }, 0);

    return ingredientsCost + recipesCost;
  }

  // Cambiar modo de creación
  switchMode(mode: 'basic' | 'advanced') {
    this.creationMode.set(mode);

    if (mode === 'advanced') {
      // En modo avanzado, el precio de compra se calcula automáticamente
      this.productForm.get('purchasePrice')?.clearValidators();
      this.productForm.get('purchasePrice')?.updateValueAndValidity();
    } else {
      // En modo básico, el precio de compra es requerido
      this.productForm.get('purchasePrice')?.setValidators([Validators.required, Validators.min(0.00001)]);
      this.productForm.get('purchasePrice')?.updateValueAndValidity();
    }
  }

  // Métodos para ingredientes
  addIngredient(ingredient: Ingredient) {
    const current = this.selectedIngredients();
    const exists = current.find(i => i.uuid === ingredient.uuid);
    if (exists) {
      console.log('⚠️ El ingrediente ya está agregado');
      return;
    }

    const newIngredient: SelectedIngredient = {
      ...ingredient,
      quantity: 0
    };

    this.selectedIngredients.set([...current, newIngredient]);
    console.log('✅ Ingrediente agregado:', ingredient.name);
  }

  removeIngredient(uuid: string) {
    const current = this.selectedIngredients();
    this.selectedIngredients.set(current.filter(i => i.uuid !== uuid));
    console.log('🗑️ Ingrediente removido');
  }

  updateIngredientQuantity(uuid: string, quantity: number) {
    const current = this.selectedIngredients();
    const updated = current.map(i =>
      i.uuid === uuid ? {...i, quantity} : i
    );
    this.selectedIngredients.set(updated);
  }

  getAvailableIngredients(): Ingredient[] {
    const selected = this.selectedIngredients();
    const selectedUuids = selected.map(i => i.uuid);
    const searchTerm = this.ingredientSearchTerm().toLowerCase();

    return this.availableIngredients()
      .filter(i => !selectedUuids.includes(i.uuid))
      .filter(i => {
        if (!searchTerm) return true;
        return i.name.toLowerCase().includes(searchTerm);
      });
  }

  updateIngredientSearchTerm(term: string) {
    this.ingredientSearchTerm.set(term);
  }

  clearIngredientSearch() {
    this.ingredientSearchTerm.set('');
  }

  // Métodos para recetas
  addRecipe(recipe: Recipe) {
    const current = this.selectedRecipes();
    const exists = current.find(r => r.uuid === recipe.uuid);
    if (exists) {
      console.log('⚠️ La receta ya está agregada');
      return;
    }

    const newRecipe: SelectedRecipe = {
      ...recipe,
      multiplier: 1
    };

    this.selectedRecipes.set([...current, newRecipe]);
    console.log('✅ Receta agregada:', recipe.name);
  }

  removeRecipe(uuid: string) {
    const current = this.selectedRecipes();
    this.selectedRecipes.set(current.filter(r => r.uuid !== uuid));
    console.log('🗑️ Receta removida');
  }

  updateRecipeMultiplier(uuid: string, multiplier: number) {
    const current = this.selectedRecipes();
    const updated = current.map(r =>
      r.uuid === uuid ? {...r, multiplier} : r
    );
    this.selectedRecipes.set(updated);
  }

  getAvailableRecipes(): Recipe[] {
    const selected = this.selectedRecipes();
    const selectedUuids = selected.map(r => r.uuid);
    const searchTerm = this.recipeSearchTerm().toLowerCase();

    return this.availableRecipes()
      .filter(r => !selectedUuids.includes(r.uuid))
      .filter(r => {
        if (!searchTerm) return true;
        return r.name.toLowerCase().includes(searchTerm);
      });
  }

  updateRecipeSearchTerm(term: string) {
    this.recipeSearchTerm.set(term);
  }

  clearRecipeSearch() {
    this.recipeSearchTerm.set('');
  }

  // Obtener el texto de la unidad
  getUnitText(unit: string): string {
    switch (unit) {
      case 'g':
        return 'gramos';
      case 'ml':
        return 'mililitros';
      case 'unit':
        return 'unidades';
      default:
        return unit;
    }
  }

  onSubmit() {
    if (this.productForm.valid) {
      const formValue = this.productForm.getRawValue();

      if (this.creationMode() === 'advanced') {
        // Modo avanzado: no enviar purchasePrice, enviar recipes e ingredients
        const product: Partial<Product> = {
          uuid: formValue.uuid || undefined,
          name: formValue.name,
          description: formValue.description,
          category: formValue.category,
          salePrice: formValue.salePrice,
          recipes: this.selectedRecipes().map(r => ({
            recipeUuid: r.uuid,
            quantityMultiplier: r.multiplier
          })),
          ingredients: this.selectedIngredients().map(i => ({
            ingredientUuid: i.uuid,
            quantity: i.quantity
          }))
        };

        console.log('📝 Enviando producto (modo avanzado):', product);
        this.onCreateProduct.emit(product as Product);
      } else {
        // Modo básico: enviar con purchasePrice, sin recipes ni ingredients
        const product: Partial<Product> = {
          uuid: formValue.uuid || undefined,
          name: formValue.name,
          description: formValue.description,
          category: formValue.category,
          purchasePrice: formValue.purchasePrice,
          salePrice: formValue.salePrice
        };

        console.log('📝 Enviando producto (modo básico):', product);
        this.onCreateProduct.emit(product as Product);
      }
    } else {
      console.error('Formulario inválido: Por favor completa todos los campos requeridos correctamente.');
    }
  }
}
