import {inject, Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {SubmenusRepository} from "../../domain/ports/submenus.repository";
import {SubmenusAdapter} from "../../infrastructure/api/submenus.adapter";
import {Submenu} from "../../domain/models/submenu.interface";

@Injectable({
  providedIn: 'root'
})
export class CreateSubmenuUseCase {

  private readonly submenusRepo: SubmenusRepository = inject(SubmenusAdapter);

  execute(submenu: Submenu): Observable<string> {
    return this.submenusRepo.createSubmenu(submenu);
  }
}
