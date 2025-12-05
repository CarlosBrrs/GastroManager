import {ChangeDetectionStrategy, Component, inject, input, output, signal} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AbstractControl, FormGroup, NonNullableFormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';
import {CashRegister} from "../../../../domain/models/cash-register.interface";

@Component({
  selector: 'gm-cash-register-card',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './cash-register-card.component.html',
  styleUrl: './cash-register-card.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CashRegisterCardComponent {
  // Inputs reactivos
  cashRegister = input.required<CashRegister>();
  sessionSummary = input<any>(null); // Nuevo input para recibir el resumen de sesión
  // Estados locales para manejar la expansión
  isExpanded = signal(false);
  expandedMode = signal<'open' | 'close' | null>(null);
  // Outputs reactivos
  openCashRegister = output<{ uuid: string, initialAmount: number, notes: string }>();
  closeCashRegister = output<{ uuid: string, finalAmount: number, notes: string }>();
  viewDetails = output<string>();
  viewTransactions = output<string>();
  requestSessionSummary = output<string>(); // Nuevo output para solicitar resumen de sesión
  // Inyecciones
  private readonly fb = inject(NonNullableFormBuilder);
  // Formulario reactivo para abrir caja
  openCashRegisterForm = this.fb.group({
    initialAmount: this.fb.control(0, [Validators.required, Validators.min(0)]),
    notes: this.fb.control('')
  });
  // Formulario reactivo para cerrar caja
  closeCashRegisterForm = this.fb.group({
    finalAmount: this.fb.control(0, [Validators.required, Validators.min(0)]),
    notes: this.fb.control('')
  });

  onOpenClick() {
    console.log('🟢 Expandiendo caja registradora para abrir:', this.cashRegister().name);
    this.isExpanded.set(true);
    this.expandedMode.set('open');
    // Resetear formulario al abrir
    this.openCashRegisterForm.reset({
      initialAmount: 0,
      notes: ''
    });
  }

  onCloseClick() {
    console.log('🔒 Expandiendo caja registradora para cerrar:', this.cashRegister().name);
    this.isExpanded.set(true);
    this.expandedMode.set('close');

    // Solicitar resumen de sesión al componente padre
    this.requestSessionSummary.emit(this.cashRegister().uuid);

    // Resetear formulario al cerrar - dejar el monto final vacío
    this.closeCashRegisterForm.reset({
      finalAmount: undefined,
      notes: ''
    });
  }

  onConfirmOpen() {
    if (this.openCashRegisterForm.valid) {
      const formValue = this.openCashRegisterForm.getRawValue();
      console.log('🟢 Confirmando apertura de caja registradora:', this.cashRegister().name);
      console.log('📄 Datos del formulario:', formValue);

      this.openCashRegister.emit({
        uuid: this.cashRegister().uuid,
        initialAmount: formValue.initialAmount,
        notes: formValue.notes
      });

      this.isExpanded.set(false);
      this.expandedMode.set(null);
      this.openCashRegisterForm.reset();
    } else {
      console.log('❌ Formulario inválido');
      this.markFormGroupTouched(this.openCashRegisterForm);
    }
  }

  onConfirmClose() {
    if (this.closeCashRegisterForm.valid) {
      const formValue = this.closeCashRegisterForm.getRawValue();
      console.log('🔒 Confirmando cierre de caja registradora:', this.cashRegister().name);
      console.log('📄 Datos del formulario:', formValue);

      this.closeCashRegister.emit({
        uuid: this.cashRegister().uuid,
        finalAmount: formValue.finalAmount,
        notes: formValue.notes
      });

      this.isExpanded.set(false);
      this.expandedMode.set(null);
      this.closeCashRegisterForm.reset();
    } else {
      console.log('❌ Formulario inválido');
      this.markFormGroupTouched(this.closeCashRegisterForm);
    }
  }

  onCancelExpansion() {
    console.log('❌ Cancelando expansión');
    this.isExpanded.set(false);
    this.expandedMode.set(null);
    this.openCashRegisterForm.reset({
      initialAmount: 0,
      notes: ''
    });
    this.closeCashRegisterForm.reset({
      finalAmount: 0,
      notes: ''
    });
  }

  // Métodos de validación para el template
  isFieldInvalid(fieldName: string, formType: 'open' | 'close' = 'open'): boolean {
    const form: FormGroup = formType === 'open' ? this.openCashRegisterForm : this.closeCashRegisterForm;
    const field: AbstractControl | null = form.get(fieldName);
    return !!(field && field.invalid && field.touched);
  }

  getFieldError(fieldName: string, formType: 'open' | 'close' = 'open'): string {
    const form: FormGroup = formType === 'open' ? this.openCashRegisterForm : this.closeCashRegisterForm;
    const field: AbstractControl | null = form.get(fieldName);
    if (field?.errors && field.touched) {
      if (field.errors['required']) return `${fieldName} es requerido`;
      if (field.errors['min']) return `El monto debe ser mayor o igual a 0`;
    }
    return '';
  }

  onDetailsClick() {
    console.log('👁️ Viendo detalles de caja registradora:', this.cashRegister().name);
    this.viewDetails.emit(this.cashRegister().uuid);
  }

  onTransactionsClick() {
    console.log('📋 Viendo transacciones de caja registradora:', this.cashRegister().name);
    this.viewTransactions.emit(this.cashRegister().uuid);
  }

  getCashRegisterStatusClass(status: string): string {
    switch (status) {
      case 'ACTIVE':
        return 'badge bg-success';
      case 'CLOSED':
        return 'badge bg-secondary';
      case 'MAINTENANCE':
        return 'badge bg-warning';
      default:
        return 'badge bg-secondary';
    }
  }

  getCashRegisterStatusText(status: string): string {
    switch (status) {
      case 'ACTIVE':
        return 'Activa';
      case 'CLOSED':
        return 'Cerrada';
      case 'MAINTENANCE':
        return 'Mantenimiento';
      default:
        return 'Desconocido';
    }
  }

  private markFormGroupTouched(form: FormGroup) {
    Object.keys(form.controls).forEach(key => {
      const control: AbstractControl | null = form.get(key);
      control?.markAsTouched();
    });
  }
}
