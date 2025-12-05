import {Component, EventEmitter, Input, Output} from '@angular/core';
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {CurrencyPipe, DatePipe, JsonPipe} from "@angular/common";
import {DialogModule} from "primeng/dialog";
import {InputTextModule} from "primeng/inputtext";
import {ConfirmationService, PrimeTemplate} from "primeng/api";
import {Table, TableModule, TableRowSelectEvent} from "primeng/table";
import {ToolbarModule} from "primeng/toolbar";
import {Router} from "@angular/router";
import {TagModule} from "primeng/tag";
import {Order, UninvoicedOrderItem} from "../../../core/store/orders/order.model";
import {SidebarModule} from "primeng/sidebar";
import {InputNumberModule} from "primeng/inputnumber";
import {
  AbstractControl,
  FormArray,
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators
} from "@angular/forms";
import {InvoiceRequestDto} from "../../../core/model/interfaces/InvoiceRequestDto";
import {PickListModule} from "primeng/picklist";
import {AccordionModule} from "primeng/accordion";
import {Invoice} from "../../../core/store/invoice/invoice.model";
import {ProgressSpinnerModule} from "primeng/progressspinner";

interface GroupedItem {
  productItemName: string;
  quantity: number;
  unitPrice: number;
  preTaxUnitPrice: number;
  vatPerUnit: number;
  totalPreTax?: number;
  totalVAT?: number;
  totalWithVAT?: number;
}

@Component({
  selector: 'gm-orders-table',
  standalone: true,
  imports: [
    ConfirmDialogModule,
    CurrencyPipe,
    DialogModule,
    InputTextModule,
    PrimeTemplate,
    TableModule,
    ToolbarModule,
    TagModule,
    DatePipe,
    SidebarModule,
    InputNumberModule,
    ReactiveFormsModule,
    PickListModule,
    JsonPipe,
    AccordionModule,
    ProgressSpinnerModule,
  ],
  templateUrl: './order-table.component.html',
  styleUrl: './order-table.component.scss'
})
export class OrderTableComponent {

  @Output() addNew = new EventEmitter();
  @Output() delete = new EventEmitter();
  @Output() edit = new EventEmitter();
  @Output() openModal = new EventEmitter<Order>();
  @Output() toggleSidebar = new EventEmitter<string>();
  @Input() data: Order[] = [];
  @Input() sidebarVisible: boolean = false;
  @Input() selectedOrder: Order | undefined;
  invoiceForm: FormGroup;
  // selectedOrder?: Order;

  invoiceModalVisible: boolean = false;
  loading: boolean = false;
  isBilling: boolean = false;
  selectedItems: any[] = [];
  @Output() invoiceGenerated = new EventEmitter<{ orderUuid: string; payload: InvoiceRequestDto }>();
  @Input() uninvoicedItems: UninvoicedOrderItem[] = [];
  groupedItems: any[] = [];
  subtotal: number = 0;
  tipAmount: number = 0;
  taxTotal: number = 0
  tipPercentage: number = 0;
  protected invoiceTotal: number = 0;

  constructor(private readonly fb: FormBuilder, private readonly router: Router, private readonly confirmationService: ConfirmationService,) {
    this.invoiceForm = this.fb.group({
      customerName: new FormControl("", [Validators.required]),
      customerPhone: new FormControl("", [Validators.required]),
      customerEmail: new FormControl("", [Validators.required]),
      invoiceItems: new FormArray([], [Validators.required]),
      tipAmount: [0, [Validators.min(0)]]
    });

    this.invoiceForm.get('tipAmount')?.valueChanges.subscribe(value => {
      if (this.tipPercentage === 0) {
        this.tipAmount = value || 0;
      }
    });
  }

  get finalTotal(): number {
    return this.subtotal + this.taxTotal + this.tipAmount;
  }

  newOrder() {
    this.router.navigate(['orders/create-order'])
  }

  onGlobalFilter(dt: Table, $event: Event) {
    dt.filterGlobal(($event.target as HTMLInputElement).value, 'contains');
  }

  editOrder(order: any) {

  }

  toggleDetailsSidebar(uuid: string) {
    this.toggleSidebar.emit(uuid);
  }

  closeSidebar() {
    this.sidebarVisible = false;
    // this.selectedOrder = undefined;
    this.isBilling = false;
  }

  onRowSelect($event: TableRowSelectEvent) {
    console.log($event)
    this.toggleDetailsSidebar($event.data.uuid)
  }

  openInvoiceModal(order: Order) {
    this.selectedOrder = order;
    this.openModal.emit(order); // Solo notifica al padre
    this.selectedItems = [];
    this.invoiceTotal = 0;
    this.invoiceModalVisible = true;

  }

  closeInvoice() {
    // this.closeModal.emit();
  }

  /*// todo calculate diff is triggered on every action on the screen
  openInvoiceModal(orders: Order) {
    // En tu servicio o componente:
    this.selectedOrder = orders;

    // Desglosar items en unidades individuales
    this.availableItems = orders.orderItems.flatMap((item: OrderItem) =>
      Array.from({length: item.quantity}, (_, index) => ({
        ...item,
        id: `${item.productItemUuid}-${index + 1}`,
        unitPrice: item.unitPrice
      })));
    this.selectedItems = [];
    this.invoiceTotal = 0;
    this.invoiceModalVisible = true;
  }
*/
  closeInvoiceModal() {
    // this.selectedOrder = undefined;
    this.selectedItems = [];
    this.invoiceTotal = 0;
    this.invoiceModalVisible = false;
    this.invoiceForm.reset();
  }

  generateInvoice() {
    if (this.selectedOrder) {
      const payload: InvoiceRequestDto = this.invoiceForm.value;
      this.invoiceGenerated.emit({orderUuid: this.selectedOrder.uuid, payload});
    } else {
      console.log("error orden no seleccionada")
    }
    this.closeInvoiceModal();
  }

  calculateTotal() {
    // 1. Agrupar items por producto
    const grouped: { [key: string]: GroupedItem } = {};

    this.selectedItems.forEach(item => {
      const key = item.productItemUuid;

      if (!grouped[key]) {
        grouped[key] = {
          productItemName: item.productItemName,
          quantity: 0,
          unitPrice: item.unitPrice,
          preTaxUnitPrice: item.unitPrice * 0.92, // 92% sin IVA
          vatPerUnit: item.unitPrice * 0.08 // 8% IVA
        };
      }

      // ✅ Ahora obtiene la cantidad real en selectedItems
      grouped[key].quantity = this.selectedItems.filter(i => i.productItemUuid === key).length;
    });

    // 2. Formatear los datos agrupados
    this.groupedItems = Object.values(grouped).map(item => ({
      ...item,
      totalPreTax: item.preTaxUnitPrice * item.quantity,
      totalVAT: item.vatPerUnit * item.quantity,
      totalWithVAT: item.unitPrice * item.quantity
    }));

    // 3. Calcular totales base
    this.subtotal = this.groupedItems.reduce((sum, item) => sum + item.totalPreTax, 0);
    this.taxTotal = this.groupedItems.reduce((sum, item) => sum + item.totalVAT, 0);
    const baseTotal = this.subtotal + this.taxTotal;

    // 4. Calcular propina según modo seleccionado
    if (this.tipPercentage > 0) {
      this.tipAmount = (baseTotal * this.tipPercentage) / 100;
      this.invoiceForm.get('tipAmount')?.setValue(this.tipAmount, {emitEvent: false}); // Evitar ciclo
    } else {
      this.tipAmount = this.invoiceForm.get('tipAmount')?.value || 0;
    }
  }

  setTip(percentage: number) {
    this.tipPercentage = percentage;

    if (percentage === 0) {
      this.tipAmount = 0;
      this.invoiceForm.get('tipAmount')?.setValue(0, {emitEvent: false});
    } else {
      const baseTotal = this.subtotal + this.taxTotal;
      this.tipAmount = (baseTotal * percentage) / 100;
      this.invoiceForm.get('tipAmount')?.setValue(this.tipAmount, {emitEvent: false});
    }

    this.calculateTotal(); // Recalcula el total con la nueva propina
  }

  onTipAmountChange() {
    this.tipPercentage = 0;
    this.tipAmount = this.invoiceForm.get('tipAmount')?.value || 0;
  }

  updateInvoiceItems() {
    const invoiceItemsArray = this.invoiceForm.get('invoiceItems') as FormArray;

    // ✅ Crear un mapa para rastrear items existentes
    const existingItemsMap = new Map<string, FormGroup>();

    invoiceItemsArray.controls.forEach((control: AbstractControl) => {
      const orderItemUuid = control.get('orderItemUuid')?.value;
      if (orderItemUuid) {
        existingItemsMap.set(orderItemUuid, control as FormGroup);
      }
    });

    // ✅ Primero, limpiar elementos que ya no están en `selectedItems`
    const selectedUuids = new Set(this.selectedItems.map(item => item.uuid));
    invoiceItemsArray.controls = invoiceItemsArray.controls.filter(control =>
      selectedUuids.has(control.get('orderItemUuid')?.value)
    );

    this.selectedItems.forEach(item => {
      const existingControl = existingItemsMap.get(item.uuid);

      if (existingControl) {
        // ✅ Obtener la cantidad real en lugar de incrementar en 1
        const newQuantity = this.selectedItems.filter(i => i.uuid === item.uuid).length;
        existingControl.get('quantity')?.setValue(newQuantity);
      } else {
        // ✅ Agregar solo si no existe
        invoiceItemsArray.push(this.fb.group({
          orderItemUuid: new FormControl(item.uuid, [Validators.required]),
          quantity: new FormControl(1, [Validators.required, Validators.min(1)])
        }));
      }
    });

    this.calculateTotal(); // Recalcula el total de la factura
  }

  getInvoicingStatusColor(invoicingStatus: any) {
    return undefined;
  }

  viewInvoices(order: any) {

  }

  getPaymentStatusColor(paymentStatus: any) {
    return undefined;
  }

  openPaymentModal(order: any) {

  }

  payInvoice(invoice: Invoice) {

  }

  viewInvoice(invoice: Invoice) {

  }

  editInvoice(invoice: Invoice) {

  }

  cancelInvoice(invoice: Invoice) {

  }

  deleteInvoice(invoice: Invoice) {

  }
}
