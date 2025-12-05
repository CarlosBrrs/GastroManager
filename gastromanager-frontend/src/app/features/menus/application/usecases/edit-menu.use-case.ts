import {inject, Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {MenusRepository} from "../../domain/ports/menus.repository";
import {MenusAdapter} from "../../infrastructure/api/menus.adapter";
import {Menu} from "../../domain/models/menu.interface";

@Injectable({
  providedIn: 'root'
})
export class EditMenuUseCase {

  private readonly menusRepo: MenusRepository = inject(MenusAdapter);

  execute(uuid: string, menu: Menu): Observable<Menu> {
    return this.menusRepo.editMenu(uuid, menu);
  }
}
