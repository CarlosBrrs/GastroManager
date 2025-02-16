import {Component, EventEmitter, Input, Output, ViewChild} from '@angular/core';
import {Table, TableModule, TableRowSelectEvent} from "primeng/table";
import {IngredientResponseDto} from "../../../core/model/interfaces/IngredientResponseDto";
import {CurrencyPipe, DatePipe} from "@angular/common";
import {ToastModule} from "primeng/toast";
import {ToolbarModule} from "primeng/toolbar";
import {Button} from "primeng/button";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {DialogModule} from "primeng/dialog";
import {InputTextModule} from "primeng/inputtext";
import {InventoryFormComponent} from "../inventory-form/inventory-form.component";
import {Ripple} from "primeng/ripple";
import {IngredientRequestDto} from "../../../core/model/interfaces/IngredientRequestDto";
import {SidebarModule} from "primeng/sidebar";
import {IngredientItem} from "../../../core/store/inventory/ingredient.model";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";

export interface AdjustStockRequestDto {
  newStock: number,
  reason: string
}

@Component({
  selector: 'gm-inventory-table',
  standalone: true,
  imports: [
    TableModule,
    CurrencyPipe,
    ToastModule,
    ToolbarModule,
    Button,
    ConfirmDialogModule,
    DialogModule,
    InputTextModule,
    InventoryFormComponent,
    Ripple,
    SidebarModule,
    DatePipe,
    ReactiveFormsModule
  ],
  templateUrl: './inventory-table.component.html',
  styleUrl: './inventory-table.component.scss'
})
export class InventoryTableComponent {

  @Input() loading = false;
  @ViewChild('dt') table!: Table;
  filterValue: string = '';

  isModalMaximized: boolean = false;
  isModalVisible: boolean = false;
  stockForm: FormGroup;
  @Output() stockAdjustment = new EventEmitter<{ payload: AdjustStockRequestDto, uuid: string }>();
  showStockForm = false;
  selectedIngredient?: IngredientResponseDto;
  @Output() edit = new EventEmitter<{ payload: IngredientRequestDto, uuid: string }>();
  @Output() delete = new EventEmitter<string>();
  @Output() addNew = new EventEmitter<IngredientRequestDto>();

  @Input() data: IngredientItem[] = [];
  constructor(private readonly fb: FormBuilder) {
    this.stockForm = this.fb.group({
      newStock: [null, [Validators.required, Validators.min(0)]],
      reason: ['', Validators.required]
    });
  }
  openNew() {
    this.selectedIngredient = undefined;
    this.isModalVisible = true;
  }

  hideDialog(): void {
    this.isModalVisible = false;
    this.selectedIngredient = undefined;  // Desseleccionamos el ingrediente
    this.table.clear();  // Limpiamos la selección en la tabla
  }

  editIngredient(ingredient: IngredientResponseDto) {
    this.selectedIngredient = ingredient;
    this.isModalVisible = true;
  }

  deleteIngredient(ingredientUuid: string) {
    this.delete.emit(ingredientUuid);
  }

  onGlobalFilter(dt: Table, event: Event) {
    const inputValue = (event.target as HTMLInputElement).value;
    this.filterValue = inputValue;
    dt.filterGlobal(inputValue, 'contains');
  }

  // TODO: CAMBIAR TIPO y separar el uuid del ingredient, ambos parametros estan llevando el uuid

  onFormSubmit(ingredient: IngredientRequestDto) {
    if (this.selectedIngredient) {
      const ingredientData = {...ingredient};
      delete ingredientData.availableStock;
      this.edit.emit({payload: ingredientData, uuid: this.selectedIngredient.uuid});
    } else {
      this.addNew.emit(ingredient);
    }
    this.hideDialog();
    this.clearText()
  }

  isMaximized(event: any): boolean {
    this.isModalMaximized = event.maximized;
    return this.isModalMaximized;
  }

  onRowSelect($event: TableRowSelectEvent) {
    this.selectedIngredient = $event.data;
    this.openSidebar()
  }

  sidebarVisible: boolean = false;

  // Abrir el sidebar
  openSidebar() {
    this.stockForm.reset({
      newStock: '',
      reason: ''
    });
    this.showStockForm = false;
    this.sidebarVisible = true;
  }

  closeSidebar() {
    this.sidebarVisible = false;
    this.selectedIngredient = undefined;
    if (this.filterValue) {
      setTimeout(() => {
        this.onGlobalFilter(this.table, { target: { value: this.filterValue } } as unknown as Event);
      }, 0);
    }
  }

  displayInputNewStock() {
    this.showStockForm = !this.showStockForm;
  }

  clearText() {
    this.filterValue = '';
    const inputElement = document.querySelector('.p-input-icon-right input') as HTMLInputElement;
    if (inputElement) {
      inputElement.value = this.filterValue;
    }
    this.onGlobalFilter(this.table, { target: { value: this.filterValue } } as unknown as Event);
  }

  submitForm() {
    if (!this.selectedIngredient) return;

    this.stockAdjustment.emit({payload: this.stockForm.value, uuid: this.selectedIngredient.uuid});
    this.showStockForm = false;
    this.closeSidebar()
  }
}
