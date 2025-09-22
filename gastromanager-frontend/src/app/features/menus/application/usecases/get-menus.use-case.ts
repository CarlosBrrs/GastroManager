import {inject, Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {Page} from "../../../../core/model/interfaces/pagination/page.interface";
import {MenusRepository} from "../../domain/ports/menus.repository";
import {MenusAdapter} from "../../infrastructure/api/menus.adapter";
import {Menu} from "../../domain/models/menu.interface";

@Injectable({
  providedIn: 'root'
})
export class GetMenusUseCase {

  private readonly menusRepo: MenusRepository = inject(MenusAdapter);

  execute(params: { page: number, size: number }): Observable<Page<Menu>> {
    return this.menusRepo.getAllMenus(params);
  }
}
