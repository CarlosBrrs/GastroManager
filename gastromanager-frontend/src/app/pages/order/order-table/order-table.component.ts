import {Component, computed, EventEmitter, Input, Output, signal} from '@angular/core';
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {CurrencyPipe} from "@angular/common";
import {DialogModule} from "primeng/dialog";
import {InputTextModule} from "primeng/inputtext";
import {ConfirmationService, PrimeTemplate} from "primeng/api";
import {ProductItemFormComponent} from "../../product-item/product-item-form/product-item-form.component";
import {Table, TableModule} from "primeng/table";
import {ToolbarModule} from "primeng/toolbar";
import {Router} from "@angular/router";
import {TagModule} from "primeng/tag";

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
    TagModule
  ],
  templateUrl: './order-table.component.html',
  styleUrl: './order-table.component.scss'
})
export class OrderTableComponent {

  @Output() addNew = new EventEmitter();
  @Output() delete = new EventEmitter();
  @Output() edit = new EventEmitter();
  private _orders = signal<any[]>([]);
  orders = computed(() => {
    return this._orders();
  })

  @Input()
  set data(value: any[]) {
    this._orders.set(value);
  }

  selectedOrder: any;

  constructor(private router: Router, private confirmationService: ConfirmationService,) {
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
