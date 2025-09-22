import {inject, Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {Page} from "../../../../core/model/interfaces/pagination/page.interface";
import {SubmenusRepository} from "../../domain/ports/submenus.repository";
import {SubmenusAdapter} from "../../infrastructure/api/submenus.adapter";
import {Submenu} from "../../domain/models/submenu.interface";

@Injectable({
  providedIn: 'root'
})
export class GetSubmenusByParamsUseCase {

  private readonly submenusRepo: SubmenusRepository = inject(SubmenusAdapter);

  execute(params: { search: string; menuUuid: string; page: number; size: number; }): Observable<Page<Submenu>> {
    return this.submenusRepo.getAllSubmenus(params);
  }
}
