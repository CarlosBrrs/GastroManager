import {ChangeDetectionStrategy, Component, computed, inject} from '@angular/core';
import {MenusTableComponent} from "../menus-table/menus-table.component";
import {ColumnProperties} from "../../../../../core/store/inventory/inventory.store";
import {MenusStore} from "../../../../../core/store/menus/menus.store";
import {ActionButtonInfo} from "../../../../../core/model/interfaces/action-button-info.interface";
import {Router} from "@angular/router";
import {SubmenuStore} from "../../../../../core/store/submenus/submenu.store";
import {JsonPipe} from "@angular/common";
import {Menu} from "../../../domain/models/menu.interface";

@Component({
  selector: 'gm-menus-page',
  standalone: true,
  imports: [
    MenusTableComponent,
    JsonPipe,
  ],
  templateUrl: './menus-page.component.html',
  styleUrl: './menus-page.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class MenusPageComponent {

  private readonly menusStore = inject(MenusStore);
  private readonly submenuStore = inject(SubmenuStore);
  private readonly router = inject(Router);
  menus = computed(() => {
    const page = this.menusStore.currentPage();
    return this.menusStore.pages().get(page) || [];
  });
  totalRecords = computed(() => this.menusStore.totalRecords());
  loading = computed(() => this.menusStore.loading());
  selectedMenu = computed(() => this.menusStore.selectedMenu());
  submenus = computed(() => {
    const menu = this.selectedMenu();
    const page = this.submenuStore.currentPage();
    if (!menu) return [];
    const byMenu = this.submenuStore.submenusByMenu().get(menu.uuid);
    return byMenu?.get(page) ?? [];
  });

  tableColumns: ColumnProperties[] = this.menusStore.tableColumns();
  actions: ActionButtonInfo[] =
    [
      {
        action: 'edit',
        icon: 'pi pi-pencil',
        label: 'Edit',
        severity: 'info',
        onClick: (rowData) => this.router.navigate(['/menus', rowData.uuid, 'edit'])
      },
      {
        action: 'delete', icon: 'pi pi-trash', label: 'Delete', severity: 'danger', onClick: (rowData) => {
        } /* TODO implementar eliminar menu */
      },
    ];

  onSelectedMenuHandler($event: Menu) {
    this.menusStore.setSelectedMenu($event);
    const params = {search: '', menuUuid: $event.uuid, page: 0, size: 4};
    this.submenuStore.getSubmenus(params);
  }

  addSubmenuHandler($event: string) {
    this.router.navigate(['/menus', $event, 'add-submenu'])
  }
}
