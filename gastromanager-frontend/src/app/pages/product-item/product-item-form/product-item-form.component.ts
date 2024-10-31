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
    DropdownModule
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

  categories: { label: string; value: Category }[] = [];

  constructor(private fb: FormBuilder, private inventoryService: InventoryService) {

    this.productItemForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      price: [0, [Validators.required, Validators.min(0.1)]],
      ingredients: [null, Validators.required],
      category: [null, Validators.required],
      ingredientQuantities: this.fb.group({}) // Grupo dinámico para las cantidades
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
          this.availableIngredients = ingredients.data;

          if (initialItem) {
            // Preseleccionar ingredientes y cantidades para actualizar
            this.productItemForm.patchValue({
              name: initialItem.name,
              description: initialItem.description,
              price: initialItem.price,
              category: initialItem.category,
            });
            //perform logic in case of update

            // Extraer los uuids de los ingredientes del producto inicial
            const selectedUuids = initialItem.ingredients.map((ing: any) => ing.ingredientUuid);
            this.productItemForm.get('ingredients')?.setValue(selectedUuids);

            // Configuramos los controles de cantidades con la función centralizada
            this.setIngredientQuantitiesControls(selectedUuids, initialItem);
            // Actualizamos selectedIngredients con los ingredientes completos para mostrarlos preseleccionados
            this.selectedIngredients = selectedUuids.map((uuid : string) =>
              this.availableIngredients.find(ing => ing.uuid === uuid)
            );
          }
        },
      }
    )
  }

  onCancel() {
    this.formCancel.emit();
  }

  onSubmit() {
    debugger;
    const { ingredientQuantities = {}, ...formValue } = {
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
    this.setIngredientQuantitiesControls(selectedUuids);
    this.selectedIngredients = selectedUuids.map(uuid => this.availableIngredients.find(ing => ing.uuid === uuid));
    this.productItemForm.get('ingredients')?.setValue(selectedUuids);
  }

  onQuantityChange(uuid: string, quantity: InputNumberInputEvent): void {
    const quantitiesGroup = this.productItemForm.get('ingredientQuantities') as FormGroup;
    const quantity1 = quantity.value as unknown as number;
    quantitiesGroup.get(uuid)?.setValue(quantity1);
  }

  private setIngredientQuantitiesControls(selectedUuids: string[], initialItem?: any): void {
    const quantitiesGroup = this.productItemForm.get('ingredientQuantities') as FormGroup;

    // Elimina los controles existentes para asegurarse de que solo queden los actuales
    Object.keys(quantitiesGroup.controls).forEach(controlName => {
      quantitiesGroup.removeControl(controlName);
    });

    // Agrega controles nuevos basados en los UUID seleccionados
    selectedUuids.forEach(uuid => {
      const initialQuantity = initialItem?.ingredients?.find((ing: any) => ing.ingredientUuid === uuid)?.quantity
      quantitiesGroup.addControl(uuid, this.fb.control(initialQuantity, [Validators.required, Validators.min(1)]));
    });
  }
}
