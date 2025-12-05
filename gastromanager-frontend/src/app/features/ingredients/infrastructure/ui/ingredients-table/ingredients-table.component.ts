import {ChangeDetectionStrategy, Component, EventEmitter, Input, Output} from '@angular/core';
import {TableModule} from "primeng/table";
import {TableComponent} from "../../../../../shared/components/table/table.component";
import {ColumnProperties} from "../../../../../core/store/inventory/inventory.store";
import {ActionButtonInfo} from "../../../../../core/model/interfaces/action-button-info.interface";
import {Ingredient} from "../../../domain/models/ingredient.interface";


@Component({
  selector: 'gm-ingredients-table',
  standalone: true,
  imports: [
    TableModule,
    TableComponent
  ],
  templateUrl: './ingredients-table.component.html',
  styleUrl: './ingredients-table.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class IngredientsTableComponent {
  @Input() data: Ingredient[] = [];
  @Input() columns: ColumnProperties[] = [];
  @Input() totalRecords: number = 0;
  @Input() actions?: ActionButtonInfo[];
  @Input() loading: boolean = true;
  @Input() selectedIngredient!: Ingredient | null;
  @Output() onIngredientSelected = new EventEmitter<string>();
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

  onSelectedIngredient($event: string) {
    this.onIngredientSelected.emit($event);
  }
}
