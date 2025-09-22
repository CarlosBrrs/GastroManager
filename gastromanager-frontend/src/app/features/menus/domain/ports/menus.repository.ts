import {Observable} from "rxjs";
import {Page} from "../../../../core/model/interfaces/pagination/page.interface";
import {Menu} from "../models/menu.interface";

export interface MenusRepository {

  getAllMenus(params: { page: number, size: number }): Observable<Page<Menu>>;

  createMenu(menu: Menu): Observable<string>;

  getMenuById(uuid: string): Observable<Menu>;

  editMenu(uuid: string, menu: Menu): Observable<Menu>;
}
