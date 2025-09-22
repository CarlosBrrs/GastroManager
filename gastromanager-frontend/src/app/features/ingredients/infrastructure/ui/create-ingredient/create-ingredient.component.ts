import {ChangeDetectionStrategy, Component, EventEmitter, inject, Input, OnChanges, Output, SimpleChanges} from '@angular/core';
import {FormGroup, NonNullableFormBuilder, ReactiveFormsModule, Validators} from "@angular/forms";
import {Ingredient} from "../../../domain/models/ingredient.interface";
import {ProgressSpinnerModule} from "primeng/progressspinner";

@Component({
  selector: 'gm-create-ingredient',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    ProgressSpinnerModule
  ],
  templateUrl: './create-ingredient.component.html',
  styleUrl: './create-ingredient.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CreateIngredientComponent {

  @Output() onCreateIngredient = new EventEmitter<Ingredient>();
  @Output() onEditIngredient = new EventEmitter<Ingredient>();
  @Input() loading: boolean = false;
  @Input() error: string | null= null;
  private _ingredientToEdit?: Ingredient;
  @Input()
  set ingredientToEdit(ingredient: Ingredient | undefined) {
    this._ingredientToEdit = ingredient;
    if (ingredient) {
      console.log('Setting ingredient to edit:', ingredient);
      this.ingredientForm.patchValue({
        ...ingredient,
        unit: mapBackendUnitToEnum(ingredient.unit)
      });
    } else {
      this.ingredientForm.reset();
    }
  }

  get ingredientToEdit(): Ingredient | undefined {
    return this._ingredientToEdit;
  }


  fb = inject(NonNullableFormBuilder)
  ingredientForm: FormGroup = this.fb.group({
    uuid: this.fb.control('', []),
    name: this.fb.control("", [Validators.required, Validators.minLength(3)]),
    pricePerUnit: this.fb.control(0, [Validators.required, Validators.min(0)]),
    availableStock: this.fb.control(0, [Validators.required, Validators.min(0)]),
    unit: this.fb.control("", [Validators.required]),
    supplier: this.fb.control("", [Validators.required]),
    minimumStockQuantity: this.fb.control(0, [Validators.required, Validators.min(0)]),
  });


  onSubmit() {
    if (this.ingredientForm.valid) {
      const ingredient = this.ingredientForm.value as Ingredient;
      if (this.ingredientToEdit) {
        this.onEditIngredient.emit(ingredient);
      } else {
        this.onCreateIngredient.emit(ingredient);
      }
    }
  }
}

// TODO NORMALIZE OR MOVE TO A DIFFERENT FILE
export enum Unit {
  GRAMS = 'GRAMS',
  MILLILITRES = 'MILLILITRES',
  UNITS = 'UNITS',
}

// TODO NORMALIZE OR MOVE TO A DIFFERENT FILE
export function mapBackendUnitToEnum(unit: string): Unit | undefined {
  const normalized = unit.trim().toLowerCase();

  switch (normalized) {
    case 'g':
    case 'grams':
      return Unit.GRAMS;
    case 'ml':
    case 'millilitres':
    case 'milliliters':
    case 'mL':
      return Unit.MILLILITRES;
    case 'units':
      return Unit.UNITS;
    default:
      return undefined; // o lanza error si quieres forzarlo
  }
}
