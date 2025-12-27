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

  actions: ActionButtonInfo[] = [];
  private readonly ordersStore = inject(OrderStore);
  orders = computed(() => {
    const page = this.ordersStore.currentPage();
    return this.ordersStore.pages().get(page) || [];
  });
  totalRecords = computed(() => this.ordersStore.totalRecords());
  loading = computed(() => this.ordersStore.loading());
  selectedOrder = computed(() => this.ordersStore.selectedOrder());
  tableColumns: ColumnProperties[] = this.ordersStore.tableColumns();
  private readonly router = inject(Router);

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
        onClick: (rowData) => { /* implementar eliminar si necesario */
        },
        visible: (rowData) => true
      },
      {
        action: 'pay',
        icon: 'pi pi-credit-card',
        label: 'Pay',
        severity: 'success',
        onClick: (rowData) => this.router.navigate(['/payments'], {queryParams: {orderUuid: rowData.uuid}}),
        visible: (rowData) => canPay(rowData),
        disabled: (rowData) => payDisabled(rowData)
      },
      {
        action: 'print',
        icon: 'pi pi-print',
        label: '',
        severity: 'info',
        onClick: (rowData) => {
          this.changeOrderStatus(rowData.uuid);
        },
        visible: (rowData) => true
      }
    ];
  }

  handleOrderSelected($event: any) {
    // TODO: Implementar si necesitas manejar selección de orden
  }

  changeOrderStatus(orderUuid: string) {
    const changeStatus = {
      newStatus: 'PREPARING',
      reason: 'Order is being prepared'
    };

    // Suscribirse al Observable retornado por el store
    this.ordersStore.changeStatus(orderUuid, changeStatus).subscribe({
      next: (result: any) => {
        console.log('✅ [Component] Proceso completo exitoso:', result);

        // Abrir ventana popup con el PDF
        if (result.pdfData) {
          this.openPdfInPopup(result.pdfData);
        }

        // Notificar éxito al usuario
        window.alert('✅ Estado actualizado y ticket generado correctamente.\n\nEl ticket PDF se ha abierto en una ventana pequeña.');
      },
      error: (error) => {
        console.error('❌ [Component] Error en changeOrderStatus:', error);

        // Manejar diferentes tipos de errores
        if (error.type === 'TICKET_ERROR') {
          // El estado se actualizó pero el ticket falló
          window.alert('⚠️ Estado actualizado pero no se pudo generar el ticket.\n\nEl estado de la orden fue cambiado exitosamente, pero ocurrió un error al generar el ticket PDF.');
        } else {
          // Error al cambiar el estado
          const message = error?.error?.message || error?.message || 'Error desconocido al cambiar estado';
          window.alert(`❌ Error al cambiar el estado de la orden\n\n${message}`);
        }
      }
    });
  }

  private openPdfInPopup(pdfArrayBuffer: ArrayBuffer): void {
    console.log('🖼️ [Component] Abriendo PDF en ventana popup');

    // Crear blob directamente del ArrayBuffer
    const blob = new Blob([pdfArrayBuffer], {type: 'application/pdf'});

    // Crear URL temporal
    const url = window.URL.createObjectURL(blob);

    // Configurar dimensiones de la ventana popup (tamaño ticket: 80mm x variable)
    const width = 400;  // Un poco más de margen para el viewer
    const height = 600; // Altura apropiada para ver el ticket
    const left = (window.screen.width - width) / 2;
    const top = (window.screen.height - height) / 2;

    // Abrir en ventana popup pequeña centrada
    const features = `width=${width},height=${height},left=${left},top=${top},toolbar=no,menubar=no,location=no,status=no`;
    const ticketWindow = window.open(url, 'ticketPDF', features);

    // Verificar si la ventana se abrió correctamente
    if (ticketWindow) {
      console.log('✅ [Component] Ventana popup abierta exitosamente');
    } else {
      console.warn('⚠️ [Component] No se pudo abrir la ventana popup (puede estar bloqueada por el navegador)');
    }

    // Liberar URL después de que se cargue el PDF
    setTimeout(() => {
      window.URL.revokeObjectURL(url);
    }, 5000);
  }

  changePageHandler($event: { page: number; size: number }) {
    console.log($event)
    this.ordersStore.getOrders($event);
  }

}
