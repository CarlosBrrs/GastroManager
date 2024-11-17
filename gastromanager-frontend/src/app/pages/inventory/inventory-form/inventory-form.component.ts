import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {InputTextModule} from "primeng/inputtext";
import {InputTextareaModule} from "primeng/inputtextarea";
import {IngredientResponseDto} from "../../../core/model/interfaces/IngredientResponseDto";
import {ButtonDirective} from "primeng/button";
import {InputNumberModule} from "primeng/inputnumber";
import {RadioButtonModule} from "primeng/radiobutton";
import {Unit} from "../../../core/model/interfaces/UnitType";

@Component({
  selector: 'gm-inventory-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    InputTextModule,
    InputTextareaModule,
    ButtonDirective,
    InputNumberModule,
    RadioButtonModule
  ],
  templateUrl: './inventory-form.component.html',
  styleUrl: './inventory-form.component.scss'
})
export class InventoryFormComponent implements OnInit {

  @Input() initialIngredient?: IngredientResponseDto;
  @Output() formSubmit = new EventEmitter<any>(); //should be an interface with the fields of the form
  ingredientForm: FormGroup;
  @Output() formCancel = new EventEmitter<void>();
  units: Unit[] = [
    {symbol: 'g', name: 'GRAMS'},
    {symbol: 'mL', name: 'MILLILITRES'},
    {symbol: 'units', name: 'UNITS'}];


  constructor(private fb: FormBuilder) {
    this.ingredientForm = this.fb.group({
      name: new FormControl<string>("", [Validators.required]),
      availableStock: new FormControl<number>(0, [Validators.required]),
      unit: new FormControl([Validators.required]),
      supplier: new FormControl<string>("", [Validators.required]),
      minimumStockQuantity: new FormControl<number>(0, [Validators.required]),
      pricePerUnit: new FormControl<number>(0, [Validators.required, Validators.min(0.00001)]),
    });
  }

  ngOnInit(): void {
    if (this.initialIngredient) {
      this.ingredientForm.patchValue(this.initialIngredient)
    }
  }

  onCancel() {
    this.formCancel.emit();
  }

  onSubmit(): void {
    this.mapUnitFromSymbolToEnum(this.ingredientForm.value)
    console.log(this.ingredientForm.value)
    this.formSubmit.emit(this.ingredientForm.value);
  }


  private mapUnitFromSymbolToEnum(value: any) {
    const unitFound: any = this.units.find(unit => unit.symbol === value.unit);
    this.ingredientForm.value.unit = unitFound.name
  }
}
