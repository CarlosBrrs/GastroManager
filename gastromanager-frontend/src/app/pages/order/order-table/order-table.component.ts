import {Component, EventEmitter, Input, Output} from '@angular/core';
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {CurrencyPipe, DatePipe} from "@angular/common";
import {DialogModule} from "primeng/dialog";
import {InputTextModule} from "primeng/inputtext";
import {ConfirmationService, PrimeTemplate} from "primeng/api";
import {ProductItemFormComponent} from "../../product-item/product-item-form/product-item-form.component";
import {Table, TableModule} from "primeng/table";
import {ToolbarModule} from "primeng/toolbar";
import {Router} from "@angular/router";
import {TagModule} from "primeng/tag";
import {Order} from "../../../core/store/order/order.model";

@Component({
  selector: 'gm-order-table',
  standalone: true,
  imports: [
    ConfirmDialogModule,
    CurrencyPipe,
    DialogModule,
    InputTextModule,
    PrimeTemplate,
    ProductItemFormComponent,
    TableModule,
    ToolbarModule,
    TagModule,
    DatePipe
  ],
  templateUrl: './order-table.component.html',
  styleUrl: './order-table.component.scss'
})
export class OrderTableComponent {

  @Output() addNew = new EventEmitter();
  @Output() delete = new EventEmitter();
  @Output() edit = new EventEmitter();

  @Input() data: Order[] = [];

  selectedOrder: any;

  constructor(private readonly router: Router, private readonly confirmationService: ConfirmationService,) {
  }

  newOrder() {
    this.router.navigate(['orders/create-order'])
  }

  onGlobalFilter(dt: Table, $event: Event) {
    dt.filterGlobal(($event.target as HTMLInputElement).value, 'contains');
  }

  editOrder(order: any) {

  }

}
