import {ChangeDetectionStrategy, Component, EventEmitter, inject, input, Output} from '@angular/core';
import {InputNumberModule} from "primeng/inputnumber";
import {InputTextModule} from "primeng/inputtext";
import {PaginatorModule} from "primeng/paginator";
import {RadioButtonModule} from "primeng/radiobutton";
import {FormControl, FormGroup, NonNullableFormBuilder, ReactiveFormsModule, Validators} from "@angular/forms";
import {ProgressSpinnerModule} from "primeng/progressspinner";
import {Product} from "../../../domain/models/product.interface";

@Component({
  selector: 'gm-create-product-form',
  standalone: true,
  imports: [
    InputNumberModule,
    InputTextModule,
    PaginatorModule,
    RadioButtonModule,
    ReactiveFormsModule,
    ProgressSpinnerModule
  ],
  templateUrl: './create-product-form.component.html',
  styleUrl: './create-product-form.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CreateProductFormComponent {

  fb = inject(NonNullableFormBuilder)
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

  onSubmit() {
    if (this.productForm.valid) {
      const product = this.productForm.value;
      this.onCreateProduct.emit(product);
    } else {
      console.error('Formulario inválido: Por favor completa todos los campos requeridos correctamente.');
    }
  }
}
