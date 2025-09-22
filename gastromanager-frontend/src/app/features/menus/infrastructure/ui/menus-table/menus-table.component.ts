import {ChangeDetectionStrategy, Component, EventEmitter, Input, Output} from '@angular/core';
import {ColumnProperties} from "../../../../../core/store/inventory/inventory.store";
import {ActionButtonInfo} from "../../../../../core/model/interfaces/action-button-info.interface";
import {TableComponent} from "../../../../../shared/components/table/table.component";
import {TreeTableComponent} from "../../../../../shared/components/tree-table/tree-table.component";
import {Menu} from "../../../domain/models/menu.interface";
import {Submenu} from "../../../../submenus/domain/models/submenu.interface";
import {JsonPipe} from "@angular/common";

@Component({
  selector: 'gm-menus-table',
  standalone: true,
  imports: [
    TableComponent,
    TreeTableComponent,
  ],
  templateUrl: './menus-table.component.html',
  styleUrl: './menus-table.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class MenusTableComponent {

  @Input() data: Menu[] = [];
  @Input() columns: ColumnProperties[] = [];
  @Input() totalRecords: number = 0;
  @Input() actions?: ActionButtonInfo[];
  @Input() loading: boolean = true;
  @Input() selectedMenu!: Menu | null;
  @Input() submenus!: Submenu[];
  @Output() onMenuSelect = new EventEmitter<Menu>();
  @Output() onSubmenuAdd = new EventEmitter<string>();

  get displayColumns(): ColumnProperties[] {
    return this.actions ?
      [...this.columns, {field: 'actions', header: 'Acciones', sortable: false}] :
      this.columns;
  }

  get processedData() {
    return this.data
  }

  /*  get processedData(): TreeNode<Menu>[] {
      const map: TreeNode[] = this.data.map(menu => ({
        "data": {
          "name": menu.name,
          "description": menu.description
        },
        "children": [
          {
            "data": {
              "name": 'item1',
              "description": 'description1'
            }
          }]
      }));
      return map

    }*/

  /*  get processedData(): TreeNode<Menu>[] {
      return this.data.map(menu => ({
        key: menu.uuid,
        label: menu.name,
        data: {
          uuid: menu.uuid,
          name: menu.name,
          description: menu.description
        },
        expanded: true,
        children: [
          {
            key: `${menu.uuid}-sub1`,
            label: 'Submenu 1',
            data: {
              uuid:`${menu.uuid}-sub1`,
              name: 'Submenu 1',
              description: 'Example submenu'
            },
            expanded: true,
            children: [
              {
                key: `${menu.uuid}-item1`,
                label: 'Item A',
                data: {
                  uuid:`${menu.uuid}-item1`,
                  name: 'Item A',
                  description: 'First dummy item'
                },
                leaf: true
              },
              {
                key: `${menu.uuid}-item2`,
                label: 'Item B',
                data: {
                  uuid:`${menu.uuid}-item2`,
                  name: 'Item B',
                  description: 'Second dummy item'
                },
                leaf: true
              }
            ]
          }
        ]
      }));
    }*/

  onSelectedMenu($event: Menu) {
    this.onMenuSelect.emit($event);
  }

  editSubmenu($event: Submenu) {
    // this.onSubmenuAdd.emit($event);
  }

  addSubmenu($event: string) {
    this.onSubmenuAdd.emit($event);
  }
}
