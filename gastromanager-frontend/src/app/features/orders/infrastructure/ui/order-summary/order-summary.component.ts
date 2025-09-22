import { ChangeDetectionStrategy, Component, EventEmitter, input, Output, inject, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, NonNullableFormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { OrderItem } from '../../../domain/models/order.models';
import {Order} from "../../../domain/models/order.interface";

@Component({
  selector: 'gm-order-summary',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, ProgressSpinnerModule],
  templateUrl: './order-summary.component.html',
  styleUrl: './order-summary.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class OrderSummaryComponent {

  orderItems = input<OrderItem[]>([]);
  loading = input<boolean>(false);
  error = input<string | null>(null);
  restaurantConfig = input<any>(null);

  @Output() itemRemoved = new EventEmitter<string>();
  @Output() quantityChanged = new EventEmitter<{productUuid: string, quantity: number}>();
  @Output() orderSubmitted = new EventEmitter<{orderData: Order, requiresPayment: boolean}>();

  private readonly fb = inject(NonNullableFormBuilder);

  // Computed para verificar si requiere pago previo
  requiresPaymentBeforeOrder = computed(() => {
    const config = this.restaurantConfig();
    return config?.configs?.requiresPaymentBeforeOrder === true;
  });

  orderForm: FormGroup = this.fb.group({
    tableNumber: this.fb.control<string>('', { validators: [Validators.required] }),
    customerName: this.fb.control<string>('', { validators: [Validators.required] }),
    orderNotes: this.fb.control<string>('')
  });

  completeOrderData = computed(() => {
    const formData = this.orderForm.getRawValue();
    const items = this.orderItems();

    return {
      ...formData,
      items: items.map(item => {
        const notesInput = document.getElementById(`notes-${item.product.uuid}`) as HTMLInputElement;
        const notes = notesInput?.value || '';

        return {
          productUuid: item.product.uuid,
          productName: item.product.name,
          quantity: item.quantity,
          unitPrice: item.product.salePrice,
          subtotal: item.subtotal,
          notes: notes
        };
      })
    };
  });

  getTotal(): number {
    return this.orderItems().reduce((sum, item) => sum + item.subtotal, 0);
  }

  removeItem(productUuid: string): void {
    this.itemRemoved.emit(productUuid);
  }

  modifyQuantity(productUuid: string, currentQuantity: number, change: number): void {
    const newQuantity = currentQuantity + change;
    if (newQuantity > 0) {
      this.quantityChanged.emit({ productUuid, quantity: newQuantity });
    }
  }

  onSubmitOrder(): void {
    if (this.orderForm.valid && this.orderItems().length > 0) {
      const orderData: Order = this.completeOrderData();
      const requiresPayment = this.requiresPaymentBeforeOrder();

      // Emitir tanto los datos de la orden como si requiere pago
      this.orderSubmitted.emit({
        orderData,
        requiresPayment
      });
    } else {
      console.log("Formulario inválido o sin productos");
    }
  }

}
