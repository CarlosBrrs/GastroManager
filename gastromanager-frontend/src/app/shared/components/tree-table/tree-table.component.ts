import {ChangeDetectionStrategy, Component, EventEmitter, Input, Output} from '@angular/core';
import {ColumnProperties} from "../../../core/store/inventory/inventory.store";
import {ActionButtonInfo} from "../../../core/model/interfaces/action-button-info.interface";
import {TreeTableModule} from "primeng/treetable";
import {Menu} from "../../../features/menus/domain/models/menu.interface";
import {Submenu} from "../../../features/submenus/domain/models/submenu.interface";

// interface Submenu {
//   uuid: string;
//   name: string;
//   description: string;
//   menuUuid: string; // referencia al menú padre
//   // Puedes agregar más campos luego como:
//   // createdAt?: string;
//   // updatedAt?: string;
//   // productCount?: number;
// }

@Component({
  selector: 'gm-tree-table',
  standalone: true,
  imports: [
    TreeTableModule,

  ],
  templateUrl: './tree-table.component.html',
  styleUrl: './tree-table.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TreeTableComponent {

  @Input() submenus!: Submenu[];
  @Input() data: Menu[] = [];
  @Input() columns: ColumnProperties[] = [];
  @Input() totalRecords: number = 0;
  @Input() loading: boolean = true;
  @Input() actions?: ActionButtonInfo[];
  @Input() selectedMenu!: Menu | null;
  @Output() onSelectedMenu = new EventEmitter<Menu>();
  @Output() onSubmenuEdit = new EventEmitter<Submenu>();
  @Output() onSubmenuAdd = new EventEmitter<string>();
  // Mapa de menú UUID a lista de submenús
  submenusMap: { [menuId: string]: Submenu[] } = {};
  // Estado de visibilidad por menú
  expandedMenus: Set<string> = new Set();

  selectMenu(menu: Menu) {

    this.selectedMenu = menu;
    this.onSelectedMenu.emit(menu);
  }

  loadSubmenus(menuUuid: string) {
    console.log(`Cargando submenús para el menú: ${menuUuid}`);
    if (this.submenusMap[menuUuid]) {
      this.toggle(menuUuid);
      return;
    }

    // Aquí deberías llamar a tu servicio (simulado por ahora)
    this.fetchSubmenus(menuUuid).then(submenus => {
      this.submenusMap[menuUuid] = submenus;
      this.toggle(menuUuid);
    });
  }

  isExpanded(menuId: string): boolean {
    return this.expandedMenus.has(menuId);
  }

  editSubmenu(submenu: Submenu) {
    this.onSubmenuEdit.emit(submenu);
  }

  deleteSubmenu(submenu: Submenu) {

  }

  goToSubmenu(submenu: Submenu) {

  }

  editMenu(menu: Menu) {

  }

  deleteMenu(menu: Menu) {

  }

  addSubmenu(menuUuid: string) {
    this.onSubmenuAdd.emit(menuUuid);
  }

  private toggle(menuId: string): void {
    if (this.expandedMenus.has(menuId)) {
      this.expandedMenus.delete(menuId);
    } else {
      this.expandedMenus.add(menuId);
    }
  }

  // Simula carga
  private fetchSubmenus(menuId: string): Promise<Submenu[]> {
    return Promise.resolve([
      // { uuid: '1', name: 'Submenu A', description: 'Submenu desc A', menuUuid: menuId },
      // { uuid: '2', name: 'Submenu B', description: 'Submenu desc B', menuUuid: menuId },
    ]);
  }
}
