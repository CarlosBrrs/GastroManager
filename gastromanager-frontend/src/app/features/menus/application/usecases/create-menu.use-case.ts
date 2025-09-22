import {inject, Injectable} from "@angular/core";
import {MenusRepository} from "../../domain/ports/menus.repository";
import {MenusAdapter} from "../../infrastructure/api/menus.adapter";
import {Observable} from "rxjs";
import {Menu} from "../../domain/models/menu.interface";

@Injectable({
  providedIn: 'root'
})
export class CreateMenuUseCase {

  private readonly menusRepo: MenusRepository = inject(MenusAdapter);

  execute(menu: Menu): Observable<string> {
    return this.menusRepo.createMenu(menu);
  }
}
