import {inject, Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {MenusRepository} from "../../domain/ports/menus.repository";
import {MenusAdapter} from "../../infrastructure/api/menus.adapter";
import {Menu} from "../../domain/models/menu.interface";

@Injectable({
  providedIn: 'root'
})
export class GetMenuByUuidUseCase {

  private readonly menusRepo: MenusRepository = inject(MenusAdapter);

  execute(uuid: string): Observable<Menu> {
    return this.menusRepo.getMenuById(uuid);
  }
}
