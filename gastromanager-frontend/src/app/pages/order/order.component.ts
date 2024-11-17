import {Component, OnInit, signal} from '@angular/core';
import {AccordionModule} from "primeng/accordion";
import {ButtonModule} from "primeng/button";
import {TabViewModule} from "primeng/tabview";
import {AvatarModule} from "primeng/avatar";
import {BadgeModule} from "primeng/badge";
import {CardModule} from "primeng/card";
import {ProductItemTableComponent} from "../product-item/product-item-table/product-item-table.component";
import {OrderTableComponent} from "./order-table/order-table.component";
import {ToastModule} from "primeng/toast";
import {finalize, Subject, takeUntil} from "rxjs";
import {OrderService} from "../../core/services/order/order.service";
import {OrderResponseDto} from "../../services/models/order-response-dto";
import {MessageService} from "primeng/api";

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
export class OrderComponent implements OnInit {
  private loading = signal<boolean>(false);
  private destroy$ = new Subject<void>();
  private error = signal<string | null>(null);

  constructor(private orderService: OrderService, private messageService: MessageService) {
  }

  ngOnInit() {
    this.loadOrders()
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

orders = signal<OrderResponseDto[]>([])

  private loadOrders() {
    this.loading.set(true)
    this.orderService.getAllOrders()
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => this.loading.set(false)))
      .subscribe({
        next: response => {
          this.orders.set(response.data);
          this.loading.set(false)
        },
        error: error => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error loading ingredients',
            detail: error.error.message
          });
          console.log("error loading ingredients", error)
          this.error.set('Error loading ingredients');
        },
        complete: () => {
          console.log("completed successfully")
        }
      })
  }
}
