import {Component, effect, inject, OnDestroy, OnInit} from '@angular/core';
import {AccordionModule} from "primeng/accordion";
import {ButtonModule} from "primeng/button";
import {TabViewModule} from "primeng/tabview";
import {AvatarModule} from "primeng/avatar";
import {BadgeModule} from "primeng/badge";
import {CardModule} from "primeng/card";
import {ToastModule} from "primeng/toast";
import {MessageService} from "primeng/api";
import {OrdersStore} from "../../core/store/orders/ordersStore";
import {StoreEventService} from "../../core/services/store-event/store-event.service";
import {Subject, takeUntil} from "rxjs";
import {InvoiceRequestDto} from "../../core/model/interfaces/InvoiceRequestDto";
import {Order, UninvoicedOrderItem} from "../../core/store/orders/order.model";

@Component({
  selector: 'gm-orders',
  standalone: true,
  imports: [
    AccordionModule,
    ButtonModule,
    TabViewModule,
    AvatarModule,
    BadgeModule,
    CardModule,
    ToastModule
  ],
  templateUrl: './order.component.html',
  styleUrl: './order.component.scss'
})
export class OrderComponent implements OnInit, OnDestroy {

  orderStore = inject(OrdersStore)
  invoiceModalVisible = false;
  selectedOrder: Order | undefined = undefined;
  uninvoicedItems: UninvoicedOrderItem[] = [];
  sidebarVisible: boolean = false;
  private readonly destroy$ = new Subject<void>();

  constructor(private readonly messageService: MessageService, private readonly storeEventService: StoreEventService) {
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

  ngOnInit() {
    this.orderStore.loadOrders()
      .pipe(
        takeUntil(this.destroy$)
      ).subscribe(orders => {
      console.log(orders)
    });
  }

  handleOpenInvoiceModal(order: Order) {
    this.selectedOrder = order;
    this.invoiceModalVisible = true; // Abre el modal inmediatamente (opcional: agregar un loading)

    this.orderStore.getUninvoicedOrderItems(order.uuid).subscribe(uninvoicedItems => {
      console.log(uninvoicedItems)
      this.uninvoicedItems = uninvoicedItems.flatMap((item: UninvoicedOrderItem) =>
        Array.from({length: item.quantity}, () => ({
          ...item,
        }))
      );
    });
  }

  handleCloseInvoiceModal() {
    this.invoiceModalVisible = false;
    // this.selectedOrder = null;
    this.uninvoicedItems = [];
  }

  handleAdd($event: any) {
    alert("method to handle add orders")
    return null;
  }

  handleDelete($event: any) {
    alert("method to delete orders")
    return null;
  }

  handleEdit($event: any) {
    alert("method to edit orders")
    return null;
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  handleInvoices(data: { orderUuid: string; payload: InvoiceRequestDto }) {
    console.log(data)
    this.orderStore.generateInvoices(data.orderUuid, data.payload).subscribe(() => {
      this.handleCloseInvoiceModal()
    });
  }

  handleToggleSidebar(uuid: string) {
    if (!uuid) {
      this.sidebarVisible = false;
      this.selectedOrder = undefined;
    } else {
      this.sidebarVisible = true;
      this.selectedOrder = undefined; // activa el estado "cargando"
      this.orderStore.getOrderDetails(uuid).subscribe(order => {
        this.selectedOrder = order;
      });
    }
  }
}
