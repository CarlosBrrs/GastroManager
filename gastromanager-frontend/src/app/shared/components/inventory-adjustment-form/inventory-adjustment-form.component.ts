import {ChangeDetectionStrategy, Component, effect, inject, input} from '@angular/core';
import {NonNullableFormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {SelectButtonModule} from 'primeng/selectbutton';
import {InputNumberModule} from 'primeng/inputnumber';
import {InputTextareaModule} from 'primeng/inputtextarea';
import {ButtonModule} from 'primeng/button';
import {FloatLabelModule} from 'primeng/floatlabel';

export interface InventoryAdjustmentFormValue {
  adjustmentType: 'replace' | 'adjust';
  quantity: number;
  reason: string;
}

@Component({
  selector: 'gm-inventory-adjustment-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    SelectButtonModule,
    InputNumberModule,
    InputTextareaModule,
    ButtonModule,
    FloatLabelModule
  ],
  templateUrl: './inventory-adjustment-form.component.html',
  styleUrl: './inventory-adjustment-form.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class InventoryAdjustmentFormComponent {
  // Input para recibir el callback desde el padre
  onSubmit = input.required<(formValue: InventoryAdjustmentFormValue) => void>();
  // Input para callback de cancelación
  onCancel = input<(() => void) | undefined>(undefined);
  // Input opcional para unidad de medida
  unit = input<string>('');
  // Input opcional para estado de loading
  loading = input<boolean>(false);
  // Input opcional para mensaje de error
  error = input<string | null>(null);
  // Opciones para el SelectButton
  adjustmentTypeOptions = [
    {label: 'Reemplazar', value: 'replace'},
    {label: 'Ajustar', value: 'adjust'}
  ];
  // Inyectar FormBuilder (patrón de create-product-form)
  private readonly fb = inject(NonNullableFormBuilder);
  // Form reactivo (patrón de create-product-form)
  form = this.fb.group({
    adjustmentType: this.fb.control<'replace' | 'adjust'>('replace', [Validators.required]),
    quantity: this.fb.control(0, [Validators.required, Validators.min(0.01)]),
    reason: this.fb.control('', [Validators.required, Validators.minLength(3)])
  });

  constructor() {
    // Effect para habilitar/deshabilitar controles según loading
    effect(() => {
      const isLoading = this.loading();

      if (isLoading) {
        this.form.disable();
      } else {
        this.form.enable();
      }
    });
  }

  get isReplaceMode(): boolean {
    return this.form.value.adjustmentType === 'replace';
  }

  get adjustmentTypeLabel(): string {
    return this.isReplaceMode ? 'Reemplazar valor' : 'Ajustar stock';
  }

  handleSubmit() {
    if (this.form.valid) {
      const callback = this.onSubmit();
      callback(this.form.getRawValue());
    } else {
      // Marcar todos los campos como touched para mostrar errores
      Object.keys(this.form.controls).forEach(key => {
        this.form.get(key)?.markAsTouched();
      });
    }
  }

  handleCancel() {
    this.form.reset({
      adjustmentType: 'replace',
      quantity: 0,
      reason: ''
    });

    // Ejecutar callback onCancel si existe
    const cancelCallback = this.onCancel();
    if (cancelCallback) {
      cancelCallback();
    }
  }
}

