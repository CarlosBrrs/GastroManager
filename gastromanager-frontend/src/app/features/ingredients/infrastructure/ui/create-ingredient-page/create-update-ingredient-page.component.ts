import {ChangeDetectionStrategy, Component, computed, inject, OnInit, Signal} from '@angular/core';
import {CreateIngredientComponent} from "../create-ingredient/create-ingredient.component";
import {InventoryStore} from "../../../../../core/store/inventory/inventory.store";
import {Ingredient} from "../../../domain/models/ingredient.interface";
import {ActivatedRoute, Router} from "@angular/router";
import {JsonPipe} from "@angular/common";

@Component({
  selector: 'gm-create-ingredient-page',
  standalone: true,
  imports: [
    CreateIngredientComponent,
    JsonPipe
  ],
  templateUrl: './create-update-ingredient-page.component.html',
  styleUrl: './create-update-ingredient-page.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CreateUpdateIngredientPageComponent implements OnInit {

  private readonly ingredientsStore = inject(InventoryStore);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  ingredientToEdit = this.ingredientsStore.ingredientToEdit as Signal<Ingredient | undefined>;
  loading = computed(() => this.ingredientsStore.loading());
  error = computed(() => this.ingredientsStore.error());

  ngOnInit(): void {
    const uuid = this.route.snapshot.paramMap.get('uuid');
    if (uuid) {
      console.log('fetching ing to edit with UUID:', uuid);
      this.ingredientsStore.getIngredientById({uuid, forEdit: true});
    } else {
      this.ingredientsStore.clearIngredientToEdit();
    }
  }

  createIngredientHandler(ingredient: Ingredient) {
    this.ingredientsStore.createIngredient(ingredient).subscribe(
      {
        next: () => {
          console.log('Ingredient added successfully:', ingredient);
          this.router.navigate(['/ingredients']);
        },
        error: (error) => {
          console.error('Error editing ingredient:', error);
          // Optionally, you can show an error message to the user
        },
        complete: () => {
          console.log('Edit operation completed');
          // Optionally, you can navigate back or show a success message
        }
      }
    );
  }

  editIngredientHandler(ingredient: Ingredient) {
    this.ingredientsStore.editIngredient(ingredient).subscribe(
      {
        next: () => {
          console.log('Ingredient edited successfully:', ingredient);
          this.ingredientsStore.clearIngredientToEdit();
          this.router.navigate(['/ingredients']);
        },
        error: (error) => {
          console.error('Error editing ingredient:', error);
          // Optionally, you can show an error message to the user
        },
        complete: () => {
          console.log('Edit operation completed');
          // Optionally, you can navigate back or show a success message
        }
      }
    );
  }
}
