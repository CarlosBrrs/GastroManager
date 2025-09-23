import {ChangeDetectionStrategy, Component, computed, inject} from '@angular/core';
import {OrdersTableComponent} from "../orders-table/orders-table.component";
import {Router} from "@angular/router";
import {ColumnProperties} from "../../../../../core/store/inventory/inventory.store";
import {ActionButtonInfo} from "../../../../../core/model/interfaces/action-button-info.interface";
import {OrderStore} from "../../../../../core/store/order/order.store";

@Component({
  selector: 'gm-orders-page',
  standalone: true,
  imports: [
    OrdersTableComponent
  ],
  templateUrl: './orders-page.component.html',
  styleUrl: './orders-page.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class OrdersPageComponent {

  private readonly ordersStore = inject(OrderStore);
  private readonly router = inject(Router);
  orders = computed(() => {
    const page = this.ordersStore.currentPage();
    return this.ordersStore.pages().get(page) || [];
  });
  totalRecords = computed(() => this.ordersStore.totalRecords());
  loading = computed(() => this.ordersStore.loading());
  selectedOrder = computed(() => this.ordersStore.selectedOrder());
  tableColumns: ColumnProperties[] = this.ordersStore.tableColumns();

  actions: ActionButtonInfo[] = [];

  constructor() {
    // Definir predicates reutilizables
    const canEdit = (r: any) => r?.operationalStatus !== 'PENDING';
    const canPay = (r: any) => r?.paymentStatus !== 'FULLY_PAID' && r?.operationalStatus !== 'CANCELLED';
    const payDisabled = (r: any) => !(r?.remainingToPay > 0);

    this.actions = [
      {
        action: 'edit',
        icon: 'pi pi-pencil',
        label: 'Edit',
        severity: 'info',
        onClick: (rowData) => this.router.navigate(['/orders/', rowData.uuid, 'edit']),
        visible: (rowData) => canEdit(rowData)
      },
      {
        action: 'delete',
        icon: 'pi pi-trash',
        label: 'Delete',
        severity: 'danger',
        onClick: (rowData) => { /* implementar eliminar si necesario */ },
        visible: (rowData) => true
      },
      {
        action: 'pay',
        icon: 'pi pi-credit-card',
        label: 'Pay',
        severity: 'success',
        onClick: (rowData) => this.router.navigate(['/payments'], { queryParams: { orderUuid: rowData.uuid } }),
        visible: (rowData) => canPay(rowData),
        disabled: (rowData) => payDisabled(rowData)
      }
    ];
  }

  handleOrderSelected($event: any) {
    // TODO: Implementar si necesitas manejar selección de orden
  }

  changePageHandler($event: { page: number; size: number }) {
    console.log($event)
    this.ordersStore.getOrders($event);
  }

}
