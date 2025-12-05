import {ChangeDetectionStrategy, Component, input, output} from '@angular/core';
import {CommonModule, NgComponentOutlet} from '@angular/common';
import {SidebarModule} from 'primeng/sidebar';
import {LocalDateTimePipe} from '../../pipes/local-date-time.pipe';
import {CustomCurrencyPipe, OrderItemsPipe, OrderStatusPipe} from '../../pipes/table-transform.pipes';
import {StockStatusPipe} from '../../pipes/stock-status.pipe';
import {UnitPipe} from '../../pipes/unit.pipe';

export interface DetailField {
  label: string;
  value: any;
  icon?: string;
  pipe?: string; // Nombre del pipe a aplicar (ej: 'currency', 'localDateTime', 'unit', etc.)
  pipeArgs?: any; // Argumentos para el pipe (ej: formato de fecha)
  format?: (value: any) => string; // Función custom de formateo
  highlight?: boolean;
  prefix?: string; // Texto/HTML antes del valor
  suffix?: string; // Texto/HTML después del valor
}

export interface DetailAction {
  component: any | (() => Promise<any>); // Componente directo o lazy loaded (Promise)
  onSubmit: (formValue: any) => void; // Callback cuando se envía el formulario
  onCancel?: () => void; // Callback cuando se cancela el formulario (opcional)
  inputs?: Record<string, any>; // Inputs adicionales para el componente
}

export interface DetailSection {
  title: string;
  icon?: string;
  fields?: DetailField[]; // Opcional - puede tener campos o acciones
  actions?: DetailAction[]; // Opcional - puede tener múltiples formularios
}

@Component({
  selector: 'gm-entity-detail-sidebar',
  standalone: true,
  imports: [CommonModule, SidebarModule, NgComponentOutlet],
  templateUrl: './entity-detail-sidebar.component.html',
  styleUrl: './entity-detail-sidebar.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class EntityDetailSidebarComponent {
  visible = input<boolean>(false);
  title = input<string>('Detalles');
  subtitle = input<string>('');
  sections = input<DetailSection[]>([]);
  loading = input<boolean>(false);

  onHide = output<void>();

  // Instancias de pipes
  private localDateTimePipe = new LocalDateTimePipe();
  private currencyPipe = new CustomCurrencyPipe();
  private unitPipe = new UnitPipe();
  private stockStatusPipe = new StockStatusPipe();
  private orderItemsPipe = new OrderItemsPipe();
  private orderStatusPipe = new OrderStatusPipe();

  handleHide() {
    this.onHide.emit();
  }

  formatValue(field: DetailField): string {
    if (!field.value && field.value !== 0) return 'N/A';

    let transformedValue = '';

    // Si hay función custom de formateo, usarla
    if (field.format) {
      transformedValue = field.format(field.value);
    }
    // Si hay pipe especificado, aplicarlo
    else if (field.pipe) {
      transformedValue = this.applyPipe(field.pipe, field.value, field.pipeArgs);
    }
    // Fallback: convertir a string
    else {
      transformedValue = field.value?.toString() || 'N/A';
    }

    // Agregar prefix y suffix si existen
    const prefix = field.prefix || '';
    const suffix = field.suffix || '';

    return `${prefix}${transformedValue}${suffix}`;
  }

  getFieldClass(field: DetailField): string {
    const classes = ['detail-value'];

    if (field.highlight) {
      classes.push('highlight');
    }

    // Agregar clase numeric si es un pipe de números o moneda
    if (field.pipe === 'currency' || field.pipe === 'stockStatus') {
      classes.push('numeric');
    }

    return classes.join(' ');
  }

  private applyPipe(pipeName: string, value: any, pipeArgs?: any): string {
    try {
      switch (pipeName) {
        case 'currency':
          return this.currencyPipe.transform(value);

        case 'localDateTime': {
          const dateFormat = pipeArgs || 'medium';
          return this.localDateTimePipe.transform(value, dateFormat);
        }

        case 'unit':
          return this.unitPipe.transform(value);

        case 'stockStatus': {
          // pipeArgs puede ser un número (minStock) o un objeto {minStock, unit}
          let minStock: number | undefined;
          let unit: string | undefined;

          if (typeof pipeArgs === 'object' && pipeArgs !== null) {
            minStock = pipeArgs.minStock;
            unit = pipeArgs.unit;
          } else if (typeof pipeArgs === 'number') {
            minStock = pipeArgs;
          }

          return this.stockStatusPipe.transform(value, minStock, unit);
        }

        case 'orderItems':
          return this.orderItemsPipe.transform(value);

        case 'orderStatus':
          return this.orderStatusPipe.transform(value);

        default:
          return value?.toString() || 'N/A';
      }
    } catch (error) {
      console.warn(`Error applying pipe ${pipeName}:`, error);
      return value?.toString() || 'N/A';
    }
  }
}

