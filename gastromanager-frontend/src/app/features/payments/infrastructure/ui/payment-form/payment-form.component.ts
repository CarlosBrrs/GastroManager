import {Component, inject, input, OnInit, output} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormControl, FormGroup, NonNullableFormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';

// Interface para los productos ordenados
interface OrderItem {
  uuid: string;
  productName: string;
  quantity: number;
  unitPrice: number;
  subtotal: number; // Cambiar totalPrice por subtotal para coincidir con el backend
  customerNotes: string; // Agregar customerNotes que también viene del backend
}

// Interface para los datos del pago que se enviarán al padre (actualizada para coincidir con el backend)
interface PaymentData {
  amount: number;           // Corresponde a BigDecimal amount
  paymentMethod: string;    // Corresponde a PaymentMethod paymentMethod
  tipAmount: number;        // Corresponde a BigDecimal tipAmount
  notes?: string;           // Corresponde a String notes (opcional)
  orderUuid: string;        // Corresponde a UUID orderUuid
  transactionId?: string;   // Corresponde a String transactionId (opcional)
}

@Component({
  selector: 'gm-payment-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './payment-form.component.html',
  styleUrl: './payment-form.component.scss'
})
export class PaymentFormComponent implements OnInit {
  // Inputs para recibir los datos
  orderUuid = input<string>('');
  totalAmount = input<number>(0);
  totalPaid = input<number>(0); // Nuevo input para el total pagado
  remainingToPay = input<number>(0); // Nuevo input para el restante por pagar
  customerName = input<string>('');
  tableNumber = input<string>('');
  orderCode = input<string>('');
  orderItems = input<OrderItem[]>([]);
  paymentLoading = input<boolean>(false); // Nuevo input para el estado de carga

  // Output para enviar los datos del pago al padre
  paymentSubmit = output<PaymentData>();
  // Métodos de pago disponibles (actualizados para coincidir con el backend)
  paymentMethods = [
    {value: 'CASH', label: 'Efectivo'},
    {value: 'CREDIT_CARD', label: 'Tarjeta de Crédito'},
    {value: 'DEBIT_CARD', label: 'Tarjeta de Débito'},
    {value: 'TRANSFER', label: 'Transferencia Bancaria'},
    {value: 'NEQUI', label: 'Nequi'},
    {value: 'DAVIPLATA', label: 'Daviplata'},
    {value: 'CREDIT', label: 'Crédito'}
  ];
  // FormBuilder injection
  private readonly fb = inject(NonNullableFormBuilder);
  // Payment form
  paymentForm: FormGroup = this.fb.group({
    // Campos de la orden (deshabilitados)
    orderUuid: this.fb.control('', []),
    orderCode: this.fb.control('', []),
    customerName: this.fb.control('', []),
    tableNumber: this.fb.control('', []),
    totalAmount: this.fb.control(0, []),

    // Campos editables del pago
    paymentMethod: this.fb.control('', [Validators.required]),
    paymentAmount: new FormControl<number>(0, [Validators.required, Validators.min(0.00001)]),
    tip: new FormControl<number>(0, [Validators.min(0)]),
    transactionId: this.fb.control('', []), // Campo para ID de transacción
    notes: this.fb.control('', []) // Campo para notas adicionales
  });

  ngOnInit() {
    // Poblar campos de la orden cuando se reciben los inputs
    this.paymentForm.patchValue({
      orderUuid: this.orderUuid(),
      orderCode: this.orderCode(),
      customerName: this.customerName(),
      tableNumber: this.tableNumber(),
      totalAmount: this.totalAmount(),
      paymentAmount: this.remainingToPay() // Cambiar para usar remainingToPay en lugar de totalAmount
    });

    // Deshabilitar campos que vienen del backend
    this.paymentForm.get('orderUuid')?.disable();
    this.paymentForm.get('orderCode')?.disable();
    this.paymentForm.get('customerName')?.disable();
    this.paymentForm.get('tableNumber')?.disable();
    this.paymentForm.get('totalAmount')?.disable();
  }

  // Método para obtener el total incluyendo tip
  getTotalWithTip(): number {
    const paymentAmount = this.paymentForm.get('paymentAmount')?.value || 0;
    const tip = this.paymentForm.get('tip')?.value || 0;
    return paymentAmount + tip;
  }

  // Método para verificar si el método de pago requiere transaction ID
  requiresTransactionId(): boolean {
    const paymentMethod = this.paymentForm.get('paymentMethod')?.value;
    return paymentMethod && paymentMethod !== 'CASH';
  }

  // Método para procesar el pago (actualizado para coincidir con PaymentCreateRequestDto)
  onProcessPayment(): void {
    if (this.paymentForm.valid) {
      console.log('💳 [PaymentFormComponent] Procesando pago...');
      console.log('📋 [PaymentFormComponent] Form values:', this.paymentForm.value);

      const transactionIdValue = this.paymentForm.get('transactionId')?.value;
      const notesValue = this.paymentForm.get('notes')?.value;

      const paymentData: PaymentData = {
        amount: this.paymentForm.get('paymentAmount')?.value,
        paymentMethod: this.paymentForm.get('paymentMethod')?.value,
        tipAmount: this.paymentForm.get('tip')?.value || 0,
        notes: notesValue && notesValue.trim() !== '' ? notesValue : undefined,
        orderUuid: this.orderUuid(),
        transactionId: transactionIdValue && transactionIdValue.trim() !== '' ? transactionIdValue : undefined
      };

      console.log('🚀 [PaymentFormComponent] Emitiendo datos de pago al padre:', paymentData);
      this.paymentSubmit.emit(paymentData);
    } else {
      console.warn('⚠️ [PaymentFormComponent] Formulario inválido, marcando campos como touched');
      this.paymentForm.markAllAsTouched();
    }
  }
}
