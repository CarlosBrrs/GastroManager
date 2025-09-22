import {Menu} from "../../../menus/domain/models/menu.interface";

export interface Submenu {
  uuid: string;
  name: string;
  description: string;
  menu: Menu;
  createdAt: Date;
  updatedAt: Date;
}
