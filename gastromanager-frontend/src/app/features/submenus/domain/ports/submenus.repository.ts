import {Observable} from "rxjs";
import {Page} from "../../../../core/model/interfaces/pagination/page.interface";
import {Submenu} from "../models/submenu.interface";

export interface SubmenusRepository {

  getAllSubmenus(params: { search: string, menuUuid: string, page: number, size: number }): Observable<Page<Submenu>>;

  createSubmenu(submenu: Submenu): Observable<string>;

}
