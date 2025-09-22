import {ChangeDetectionStrategy, Component, EventEmitter, Input, Output} from '@angular/core';
import {Order} from "../../../domain/models/order.interface";
import {ColumnProperties} from '../../../../../core/store/inventory/inventory.store';
import {ActionButtonInfo} from "../../../../../core/model/interfaces/action-button-info.interface";
import {TableComponent} from "../../../../../shared/components/table/table.component";

@Component({
  selector: 'gm-orders-table',
  standalone: true,
  imports: [
    TableComponent
  ],
  templateUrl: './orders-table.component.html',
  styleUrl: './orders-table.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class OrdersTableComponent {
  @Input() data: Order[] = [];
  @Input() columns: ColumnProperties[] = [];
  @Input() totalRecords: number = 0;
  @Input() actions: ActionButtonInfo[] = [];
  @Input() loading: boolean = false;
  @Input() selectedOrder!: Order | null;
  @Output() onOrderSelected = new EventEmitter<Order>();
  @Output() changePage = new EventEmitter<{ page: number, size: number }>();

  get displayColumns(): ColumnProperties[] {
    return this.actions ?
      [...this.columns, {field: 'actions', header: 'Acciones', sortable: false}] :
      this.columns;
  }

  get processedData(): Order[] {
    return this.data/*.map(order => ({
      ...order,
      orderItems: order.orderItems
        .map(item => `${item.productName} - ${item.quantity}x$${item.unitPrice} - $${item.subtotal}`)
    }));*/
  }

  dataToProcess($event: { page: number; size: number }) {
    this.changePage.emit($event);
  }

  onSelectedOrder($event: Order) {
    console.log('🔄 [OrdersTable] Order selected:', $event);
    this.onOrderSelected.emit($event);
  }
}
