import {Component, effect, inject, OnDestroy, OnInit} from '@angular/core';
import {AccordionModule} from "primeng/accordion";
import {ButtonModule} from "primeng/button";
import {TabViewModule} from "primeng/tabview";
import {AvatarModule} from "primeng/avatar";
import {BadgeModule} from "primeng/badge";
import {CardModule} from "primeng/card";
import {ProductItemTableComponent} from "../product-item/product-item-table/product-item-table.component";
import {OrderTableComponent} from "./order-table/order-table.component";
import {ToastModule} from "primeng/toast";
import {MessageService} from "primeng/api";
import {OrderStore} from "../../core/store/order/order.store";
import {StoreEventService} from "../../core/services/store-event/store-event.service";
import {Subject, takeUntil} from "rxjs";

@Component({
  selector: 'gm-order',
  standalone: true,
  imports: [
    AccordionModule,
    ButtonModule,
    TabViewModule,
    AvatarModule,
    BadgeModule,
    CardModule,
    ProductItemTableComponent,
    OrderTableComponent,
    ToastModule
  ],
  templateUrl: './order.component.html',
  styleUrl: './order.component.scss'
})
export class OrderComponent implements OnInit, OnDestroy {

  orderStore = inject(OrderStore)
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

}
