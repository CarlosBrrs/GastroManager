import {ChangeDetectionStrategy, Component, EventEmitter, Input, Output} from '@angular/core';
import {TableLazyLoadEvent, TableModule, TableRowSelectEvent} from "primeng/table";
import {Button} from "primeng/button";
import {ActionButtonInfo} from "../../../core/model/interfaces/action-button-info.interface";
import {OrderItemsPipe, CustomCurrencyPipe, OrderStatusPipe} from "../../pipes/table-transform.pipes";

@Component({
  selector: 'gm-table',
  standalone: true,
  imports: [
    TableModule,
    Button
  ],
  templateUrl: './table.component.html',
  styleUrl: './table.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TableComponent<T> {
  @Input() data: T[] = [];
  @Input() columns: any[] = [];
  @Input() totalRecords: number = 0;
  @Input() loading: boolean = true;
  @Input() actions?: ActionButtonInfo[];
  @Input() selectedItem!: T | null;
  @Output() onRowSelect = new EventEmitter<T>();
  @Output() changePage = new EventEmitter<{ page: number, size: number }>();

  get displayColumns() {
    return this.columns.filter(col => !col.hidden);
  }

  loadPageData($event: TableLazyLoadEvent) {
    const page = ($event.first || 0) / ($event.rows || 7);
    const size = $event.rows || 7;
    this.changePage.emit({page, size});
  }

  onSelectedRow($event: TableRowSelectEvent) {
    this.onRowSelect.emit($event.data);
  }

  executeAction(action: ActionButtonInfo, rowData: any, event: Event) {
    event.stopPropagation();
    action.onClick(rowData);
  }

  // Método mejorado para aplicar transformaciones con prefix/suffix
  transformCellValue(column: any, value: any): string {
    let transformedValue = '';

    // Aplicar transformación (pipe o función) usando if/else para evitar declaraciones en case
    if (column.pipe === 'orderItems') {
      try {
        transformedValue = new OrderItemsPipe().transform(value);
      } catch (error) {
        console.warn(`Error applying pipe ${column.pipe}:`, error);
        transformedValue = value?.toString() || '';
      }
    } else if (column.pipe === 'currency') {
      try {
        transformedValue = new CustomCurrencyPipe().transform(value);
      } catch (error) {
        console.warn(`Error applying pipe ${column.pipe}:`, error);
        transformedValue = value?.toString() || '';
      }
    } else if (column.pipe === 'orderStatus') {
      try {
        transformedValue = new OrderStatusPipe().transform(value);
      } catch (error) {
        console.warn(`Error applying pipe ${column.pipe}:`, error);
        transformedValue = value?.toString() || '';
      }
    } else if (column.transform) {
      try {
        transformedValue = column.transform(value);
      } catch (error) {
        console.warn(`Error applying transform function:`, error);
        transformedValue = value?.toString() || '';
      }
    } else {
      transformedValue = value?.toString() || '';
    }

    // Agregar prefix y suffix si existen
    const prefix = column.prefix || '';
    const suffix = column.suffix || '';

    return `${prefix}${transformedValue}${suffix}`;
  }
}
