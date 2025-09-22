import {ChangeDetectionStrategy, Component, EventEmitter, Input, Output} from '@angular/core';
import {ColumnProperties} from "../../../../../core/store/inventory/inventory.store";
import {ActionButtonInfo} from "../../../../../core/model/interfaces/action-button-info.interface";
import {Product} from "../../../domain/models/product.interface";
import {TableComponent} from "../../../../../shared/components/table/table.component";

@Component({
  selector: 'gm-products-table',
  standalone: true,
  imports: [
    TableComponent
  ],
  templateUrl: './products-table.component.html',
  styleUrl: './products-table.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ProductsTableComponent {
  @Input() data: Product[] = [];
  @Input() columns: ColumnProperties[] = [];
  @Input() totalRecords: number = 0;
  @Input() actions?: ActionButtonInfo[];
  @Input() loading: boolean = true;
  @Input() selectedProduct!: Product | null;
  @Output() onProductSelected = new EventEmitter<Product>();
  @Output() changePage = new EventEmitter<{ page: number, size: number }>();

  get displayColumns(): ColumnProperties[] {
    return this.actions ?
      [...this.columns, {field: 'actions', header: 'Acciones', sortable: false}] :
      this.columns;
  }

  get processedData(): any[] {
    return this.data;
  }

  dataToProcess($event: { page: number; size: number }) {
    this.changePage.emit($event);
  }

  onSelectedIngredient($event: Product) {
    this.onProductSelected.emit($event);
  }
}
