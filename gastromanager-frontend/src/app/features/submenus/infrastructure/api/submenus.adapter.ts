import {inject, Injectable} from "@angular/core";
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {catchError, Observable} from "rxjs";
import {Page} from "../../../../core/model/interfaces/pagination/page.interface";
import {SubmenusRepository} from "../../domain/ports/submenus.repository";
import {Submenu} from "../../domain/models/submenu.interface";
import {ApiGenericResponse} from "../../../../core/model/interfaces/ApiGenericResponse";
import {map} from "rxjs/operators";
import {SubmenuSummaryResponseDto} from "../../domain/models/submenu-summary-response-dto.interface";
import {mapToSubmenu, mapToSubmenuRequestDto} from "../../application/mappers/submenu.mapper";
import {MenuRequestDto} from "../../../menus/domain/models/menu-request-dto.interface";
import {mapToMenuRequestDto} from "../../../menus/application/mappers/menu.mapper";
import {SubmenuRequestDto} from "../../domain/models/submenu-request-dto.interface";

@Injectable({
  providedIn: 'root'
})
export class SubmenusAdapter implements SubmenusRepository {


  private readonly http: HttpClient = inject(HttpClient);
  private readonly baseUrl: string = 'http://localhost:8080/api/v1';

  getAllSubmenus(params: { search: string; menuUuid: string; page: number; size: number; }): Observable<Page<Submenu>> {
    const requestParams = new HttpParams()
      .set('search', params.search)
      .set('menuUuid', params.menuUuid)
      .set('page', params.page)
      .set('size', params.size);
    return this.http.get<ApiGenericResponse<Page<SubmenuSummaryResponseDto>>>(`${this.baseUrl}/submenus`,
      {
        params: requestParams
      }
    ).pipe(
      map((response: ApiGenericResponse<Page<SubmenuSummaryResponseDto>>) => {
        const mappedContent = response.data.content.map(dto => mapToSubmenu(dto));
        return {
          ...response.data,
          content: mappedContent
        };
      }),
      catchError((error: HttpErrorResponse) => {
        throw new Error(error.error.message);
      })
    )
  }

  createSubmenu(submenu: Submenu): Observable<string> {
    const mappedSubmenu: SubmenuRequestDto = mapToSubmenuRequestDto(submenu);
    return this.http.post<ApiGenericResponse<string>>(`${this.baseUrl}/submenus`, mappedSubmenu
    ).pipe(
      map((response: ApiGenericResponse<string>) => response.data),
      catchError((error: HttpErrorResponse) => {
        throw new Error(error.error.message);
      })
    )
  }

}
