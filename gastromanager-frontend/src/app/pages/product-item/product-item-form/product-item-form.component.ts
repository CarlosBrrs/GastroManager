import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ButtonDirective} from "primeng/button";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {InputNumberInputEvent, InputNumberModule} from "primeng/inputnumber";
import {InputTextModule} from "primeng/inputtext";
import {RadioButtonModule} from "primeng/radiobutton";
import {InputTextareaModule} from "primeng/inputtextarea";
import {InventoryService} from "../../../core/services/inventory/inventory.service";
import {MultiSelectModule} from "primeng/multiselect";
import {Category} from "../../../core/model/enums/Category";
import {DropdownModule} from "primeng/dropdown";
import {forkJoin, of} from "rxjs";
import {JsonPipe} from "@angular/common";

@Component({
  selector: 'gm-product-item-form',
  standalone: true,
  imports: [
    ButtonDirective,
    FormsModule,
    InputNumberModule,
    InputTextModule,
    RadioButtonModule,
    ReactiveFormsModule,
    InputTextareaModule,
    MultiSelectModule,
    DropdownModule,
    JsonPipe
  ],
  templateUrl: './product-item-form.component.html',
  styleUrl: './product-item-form.component.scss'
})
export class ProductItemFormComponent implements OnInit {
  @Input() initialProductItem?: any;
  productItemForm: FormGroup;
  @Output() formCancel = new EventEmitter<void>();
  @Output() formSubmit = new EventEmitter<unknown>();
  selectedIngredients: any[] = [];
  availableIngredients: any[] = [];
  costOfProduction: number = 0;

  categories: { label: string; value: Category }[] = [];
  suggestedPrice: number = 0;

  constructor(private readonly fb: FormBuilder, private readonly inventoryService: InventoryService) {

    this.productItemForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      price: [0, [Validators.required, Validators.min(0.1)]],
      ingredients: [null, Validators.required],
      category: [null, Validators.required],
      ingredientQuantities: this.fb.group({})
    })
  }

  ngOnInit(): void {
    this.categories = Object.entries(Category).map(([key, value]) => ({
      label: value,           // Mostrar en el frontend
      value: key as Category  // Usar el valor del enum para el form
    }));

    forkJoin({
      ingredients: this.inventoryService.getAllIngredients(),
      initialItem: this.initialProductItem ? of(this.initialProductItem) : of(null)
    }).subscribe({
        next: ({ingredients, initialItem}) => {
          this.availableIngredients = ingredients;

          if (initialItem) {
            this.productItemForm.patchValue({
              name: initialItem.name,
              description: initialItem.description,
              price: initialItem.price,
              category: initialItem.category,
            });

            const selectedUuids = initialItem.ingredients.map((ing: any) => ing.ingredientUuid);
            this.productItemForm.get('ingredients')?.setValue(selectedUuids);

            // Configuramos los controles de cantidades con la función centralizada
            this.setIngredientQuantitiesControls(selectedUuids, initialItem);
            // Actualizamos selectedIngredients con los ingredientes completos para mostrarlos preseleccionados
            this.selectedIngredients = selectedUuids.map((uuid: string) =>
              this.availableIngredients.find(ing => ing.uuid === uuid)
            );
            this.costOfProduction = this.calculateCostAndPrice();
          }
        },
      }
    )
  }

  onCancel() {
    this.formCancel.emit();
  }

  onSubmit() {
    const {ingredientQuantities = {}, ...formValue} = {
      ...this.productItemForm.value,
      ingredients: this.selectedIngredients.map(ingredient => ({
        ingredientUuid: ingredient.uuid,
        quantity: this.productItemForm.get(`ingredientQuantities.${ingredient.uuid}`)?.value
      }))
    };
    this.formSubmit.emit(formValue);
  }

  //send the uuids
  onIngredientSelect(selectedUuids: string[]): void {
    this.setIngredientQuantitiesControls(selectedUuids, this.initialProductItem);
    this.selectedIngredients = selectedUuids.map(uuid => this.availableIngredients.find(ing => ing.uuid === uuid));
    this.productItemForm.get('ingredients')?.setValue(selectedUuids);
  }

  onQuantityChange(uuid: string, quantity: InputNumberInputEvent): void {
    const quantitiesGroup = this.productItemForm.get('ingredientQuantities') as FormGroup;
    const quantity1 = quantity.value as unknown as number;
    quantitiesGroup.get(uuid)?.setValue(quantity1);
    this.costOfProduction = this.calculateCostAndPrice()
    console.log(quantitiesGroup.controls)
  }

  private setIngredientQuantitiesControls(selectedUuids: string[], initialItem?: any): void {
    const quantitiesGroup = this.productItemForm.get('ingredientQuantities') as FormGroup;

    const currentControls = Object.keys(quantitiesGroup.controls);
    currentControls.forEach(controlName => {
      if (!selectedUuids.includes(controlName)) {
        quantitiesGroup.removeControl(controlName);
      }
    });

    selectedUuids.forEach(uuid => {
      if (!quantitiesGroup.get(uuid)) {
        const initialQuantity = initialItem?.ingredients?.find((ing: any) => ing.ingredientUuid === uuid)?.quantity ?? null;


        quantitiesGroup.addControl(uuid, this.fb.control(initialQuantity, [Validators.required]));
      }
    });

    this.costOfProduction = this.calculateCostAndPrice();
  }

  calculateCostAndPrice(): number {
    let totalCost = 0;
    this.selectedIngredients.forEach(ingredient => {
      const quantity = this.productItemForm.get(`ingredientQuantities.${ingredient.uuid}`)?.value || 0;
      const ingredientCost = this.availableIngredients.find(i => ingredient.uuid === i.uuid).pricePerUnit * quantity;
      totalCost += ingredientCost;
    });

    const profitMargin = this.productItemForm.get('profitMargin')?.value || 30;  // Obtener porcentaje de ganancia
    const suggestedPrice = totalCost * (1 + profitMargin / 100);  // Precio sugerido con ganancia
    this.suggestedPrice = suggestedPrice;

    // También podrías actualizar el precio en el formulario si lo deseas:
    // this.productItemForm.get('price')?.setValue(suggestedPrice);
    return totalCost;
  }
}
