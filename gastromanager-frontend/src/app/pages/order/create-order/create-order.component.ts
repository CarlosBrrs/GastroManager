import {Component, effect, inject, OnDestroy, OnInit, signal} from '@angular/core';
import {SplitterModule} from "primeng/splitter";
import {TabViewModule} from "primeng/tabview";
import {AvatarModule} from "primeng/avatar";
import {CurrencyPipe, DatePipe, JsonPipe, NgOptimizedImage} from "@angular/common";
import {Button} from "primeng/button";
import {ProductItemService} from "../../../core/services/product-item/product-item.service";
import {finalize, Subject, takeUntil} from "rxjs";
import {ProductItemResponseDto} from "../../../core/model/interfaces/ProductItemResponseDto";
import {ConfirmationService, MessageService} from "primeng/api";
import {MenuItemCardComponent} from "../../../shared/components/menu-item-card/menu-item-card.component";
import {ProductItem} from "../../../core/model/interfaces/ProductItem";
import {FormArray, FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {InputTextareaModule} from "primeng/inputtextarea";
import {InputTextModule} from "primeng/inputtext";
import {InputNumberInputEvent, InputNumberModule} from "primeng/inputnumber";
import {OrderItem} from "../../../core/store/cart/cart.model";
import {ToastModule} from "primeng/toast";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {Router} from "@angular/router";
import {OrderService} from "../../../core/services/order/order.service";
import {ProductItemStore} from "../../../core/store/product-item/product-item.store";
import {OrdersStore} from "../../../core/store/orders/ordersStore";
import {StoreEventService} from "../../../core/services/store-event/store-event.service";

@Component({
  selector: 'gm-create-orders',
  standalone: true,
  imports: [
    SplitterModule,
    TabViewModule,
    AvatarModule,
    CurrencyPipe,
    Button,
    DatePipe,
    JsonPipe,
    MenuItemCardComponent,
    ReactiveFormsModule,
    InputTextareaModule,
    InputTextModule,
    InputNumberModule,
    FormsModule,
    NgOptimizedImage,
    ToastModule,
    ConfirmDialogModule
  ],
  templateUrl: './create-order.component.html',
  styleUrl: './create-order.component.scss'
})
export class CreateOrderComponent implements OnInit, OnDestroy {
  orderForm: FormGroup;
  productItemStore = inject(ProductItemStore)
  orderStore = inject(OrdersStore)
  private readonly destroy$ = new Subject<void>();

  constructor(private readonly storeEventService: StoreEventService,private readonly router: Router, private readonly fb: FormBuilder, private readonly confirmationService: ConfirmationService, private messageService: MessageService) {
    this.orderForm = this.fb.group({
      customerNotes: new FormControl<string>(""),
      orderItems: this.fb.array<OrderItem>([]),
    });
    // Efecto para manejar eventos de éxito
    effect(() => {
      const successMessage = this.storeEventService.successSignal();
      const successHeaderMessage = this.storeEventService.successHeaderSignal();
      if (successMessage) {
        this.messageService.add({
          severity: 'success',
          summary: successHeaderMessage,
          detail: successMessage
        });
        // Opcional: limpiar el mensaje después de mostrarlo
        this.storeEventService.successSignal.set(null);
        this.storeEventService.successHeaderSignal.set(undefined);
      }
    }, {allowSignalWrites: true});

    // Efecto para manejar eventos de error
    effect(() => {
      const errorMessage = this.storeEventService.errorSignal();
      const errorHeaderMessage = this.storeEventService.errorHeaderSignal();
      if (errorMessage) {
        this.messageService.add({
          severity: 'error',
          summary: errorHeaderMessage,
          detail: errorMessage
        });
        // Opcional: limpiar el mensaje después de mostrarlo
        this.storeEventService.errorSignal.set(null);
        this.storeEventService.errorHeaderSignal.set(undefined);
      }
    }, {allowSignalWrites: true});
  }

  ngOnInit(): void {
    this.productItemStore.loadProductItems().pipe(
      takeUntil(this.destroy$)
    ).subscribe();
  }

  selectedProducts: any[] = [];

  get orderItems() {
    return this.orderForm.get('orderItems') as FormArray;
  }

  // Método para crear un FormGroup para cada item
  createOrderItemFormGroup(menuItem: ProductItem, quantity: number = 1) {
    return this.fb.group({
      productItemUuid: [menuItem.uuid],
      quantity: [quantity]
    });
  }

  addToOrder(menuItem: ProductItem) {
    // Buscamos si el producto ya existe en selectedProducts
    const existingItemIndex = this.selectedProducts.findIndex(
      item => item.uuid === menuItem.uuid
    );

    if (existingItemIndex !== -1) {
      // Si existe, incrementamos la cantidad
      const currentQuantity = this.selectedProducts[existingItemIndex].quantity || 1;
      this.selectedProducts[existingItemIndex].quantity = currentQuantity + 1;

      // Actualizamos el FormArray
      this.orderItems.at(existingItemIndex).patchValue({
        quantity: currentQuantity + 1
      });
    } else {
      // Añadimos al array visual
      const newItem = {...menuItem, quantity: 1};
      this.selectedProducts.push(newItem);

      // Añadimos al FormArray
      this.orderItems.push(this.createOrderItemFormGroup(menuItem));
    }
  }


  removeItem(index: number) {
    this.selectedProducts.splice(index, 1);
    this.orderItems.removeAt(index);
  }

  getSubtotal(): number {
    return this.selectedProducts.reduce((total, item) => {
      return total + (item.price * (item.quantity || 1));
    }, 0);
  }

  getTax(): number {
    return this.getSubtotal() * 0.01; // 1% de impuesto
  }

  getTotal(): number {
    return this.getSubtotal() + this.getTax();
  }


  onSubmit() {
    this.orderStore.createOrder(this.orderForm.value).subscribe(() => {
      this.router.navigate(['orders'])
    });
  }

  onQuantityChange(index: number, newValue: InputNumberInputEvent) {
    const newNumber = newValue.value as number;
    this.selectedProducts[index].quantity = newNumber;
    this.orderItems.at(index).patchValue({quantity: newNumber});
  }

  private buildOrderSummary(): string {
// Obtener los detalles de los elementos de la orden desde orderForm
    const orderItems = this.orderForm.get('orderItems')?.value || [];

    // Construir el mensaje dinámico
    return orderItems.map((item: any) => {
      const product = this.selectedProducts.find(p => p.uuid === item.productItemUuid);
      if (!product) {
        return `- Product with ID ${item.productItemUuid} not found.`;
      }

      const name = product.name;
      const quantity = item.quantity || 0;
      const price = product.price || 0;
      const total = quantity * price;

      return `</br>- ${name}: ${quantity} x ${price} = ${total.toFixed(2)}`;
    }).join('\n');
  }

  confirm1($event: MouseEvent) {
    this.confirmationService.confirm({
      target: $event.target as EventTarget,
      header: 'Confirmation',
      message: `The details of your order are:</br>${this.buildOrderSummary()}`,
      icon: 'pi pi-exclamation-triangle',
      acceptIcon: "none",
      rejectIcon: "none",
      rejectButtonStyleClass: "p-button-text",
      accept: () => {
        this.onSubmit()

      },
      reject: () => {
        this.messageService.add({
          severity: 'info',
          summary: 'Order still in process ',
          detail: 'Continue setting up your orders',
          life: 3000
        });
      }
    });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
