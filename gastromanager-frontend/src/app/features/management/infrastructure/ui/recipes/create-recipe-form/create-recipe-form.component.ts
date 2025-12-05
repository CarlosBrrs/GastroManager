import {ChangeDetectionStrategy, Component, EventEmitter, inject, input, Output, signal} from '@angular/core';
import {FormControl, FormGroup, NonNullableFormBuilder, ReactiveFormsModule, Validators} from "@angular/forms";
import {ProgressSpinnerModule} from "primeng/progressspinner";
import {Recipe} from "../../../../domain/models/recipe.interface";
import {CommonModule} from "@angular/common";
import {Router} from "@angular/router";
import {Ingredient} from "../../../../../ingredients/domain/models/ingredient.interface";

interface SelectedIngredient extends Ingredient {
  quantity: number;
}

@Component({
  selector: 'gm-create-recipe-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    ProgressSpinnerModule
  ],
  templateUrl: './create-recipe-form.component.html',
  styleUrl: './create-recipe-form.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CreateRecipeFormComponent {
  fb = inject(NonNullableFormBuilder);
  // Input para recibir ingredientes disponibles desde el componente padre
  availableIngredients = input<Ingredient[]>([]);
  // Input para recibir recetas disponibles desde el componente padre
  availableRecipes = input<Recipe[]>([]);
  // Lista de ingredientes seleccionados
  selectedIngredients = signal<SelectedIngredient[]>([]);
  // Filtro de búsqueda para ingredientes
  ingredientSearchTerm = signal<string>('');
  // Receta base seleccionada
  selectedBaseRecipe = signal<Recipe | null>(null);
  recipeForm: FormGroup = this.fb.group({
    uuid: this.fb.control('', []),
    name: this.fb.control("", [Validators.required, Validators.minLength(3)]),
    description: this.fb.control("", [Validators.required, Validators.minLength(3)]),
    yieldPortions: new FormControl<number>(1, [Validators.required, Validators.min(1)]),
  });
  loading = input<boolean>(false);
  error = input<string | null>(null);
  @Output() onCreateRecipe = new EventEmitter<Partial<Recipe>>();
  private readonly router = inject(Router);

  // Computed para calcular el costo total de producción
  get totalProductionCost(): number {
    // Costo de los ingredientes seleccionados
    const ingredientsCost = this.selectedIngredients().reduce((total, ingredient) => {
      const cost = ingredient.quantity * ingredient.pricePerUnit;
      return total + cost;
    }, 0);

    // Sumar el costo de la receta base si está seleccionada
    const baseRecipeCost = this.selectedBaseRecipe()?.cost || 0;

    return ingredientsCost + baseRecipeCost;
  }

  // Método para agregar un ingrediente a la lista de seleccionados
  addIngredient(ingredient: Ingredient) {
    const current = this.selectedIngredients();

    // Verificar si ya existe el ingrediente
    const exists = current.find(i => i.uuid === ingredient.uuid);
    if (exists) {
      console.log('⚠️ El ingrediente ya está agregado');
      return;
    }

    // Agregar ingrediente con cantidad inicial 0
    const newIngredient: SelectedIngredient = {
      ...ingredient,
      quantity: 0
    };

    this.selectedIngredients.set([...current, newIngredient]);
    console.log('✅ Ingrediente agregado:', ingredient.name);
  }

  // Método para remover un ingrediente de la lista
  removeIngredient(uuid: string) {
    const current = this.selectedIngredients();
    this.selectedIngredients.set(current.filter(i => i.uuid !== uuid));
    console.log('🗑️ Ingrediente removido');
  }

  // Método para actualizar la cantidad de un ingrediente
  updateIngredientQuantity(uuid: string, quantity: number) {
    const current = this.selectedIngredients();
    const updated = current.map(i =>
      i.uuid === uuid ? {...i, quantity} : i
    );
    this.selectedIngredients.set(updated);
  }

  // Obtener ingredientes disponibles que no han sido seleccionados y filtrados por búsqueda
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

  // Actualizar el término de búsqueda
  updateSearchTerm(term: string) {
    this.ingredientSearchTerm.set(term);
  }

  // Limpiar el término de búsqueda
  clearSearch() {
    this.ingredientSearchTerm.set('');
  }

  // Seleccionar una receta base
  selectBaseRecipe(recipe: Recipe | null) {
    this.selectedBaseRecipe.set(recipe);
    console.log('📋 Receta base seleccionada:', recipe?.name || 'Ninguna');
  }

  // Manejar cambio en el select de receta base
  onBaseRecipeChange(event: Event) {
    const select = event.target as HTMLSelectElement;
    const uuid = select.value;
    if (uuid) {
      const recipe = this.availableRecipes().find(r => r.uuid === uuid);
      this.selectBaseRecipe(recipe || null);
    } else {
      this.selectBaseRecipe(null);
    }
  }

  onSubmit() {
    if (this.recipeForm.valid && this.selectedIngredients().length > 0) {
      const formValue = this.recipeForm.getRawValue();

      const recipe = {
        uuid: formValue.uuid || undefined, // Solo enviar UUID si existe (para updates)
        name: formValue.name,
        description: formValue.description,
        yieldPortions: formValue.yieldPortions,
        ingredients: this.selectedIngredients().map(i => ({
          ingredientUuid: i.uuid,
          quantity: i.quantity
        }))
      };

      console.log('📝 Enviando receta:', recipe);
      this.onCreateRecipe.emit(recipe);
    } else {
      if (!this.recipeForm.valid) {
        console.error('❌ Formulario inválido: Por favor completa todos los campos requeridos correctamente.');
      }
      if (this.selectedIngredients().length === 0) {
        console.error('❌ Debes agregar al menos un ingrediente a la receta.');
      }
    }
  }

  onCancel() {
    this.router.navigate(['/management/recipes']);
  }
}
