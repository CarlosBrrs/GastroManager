import {inject, Injectable} from "@angular/core";
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {catchError, Observable} from "rxjs";
import {Page} from "../../../../core/model/interfaces/pagination/page.interface";
import {ApiGenericResponse} from "../../../../core/model/interfaces/ApiGenericResponse";
import {map} from "rxjs/operators";
import {MenusRepository} from "../../domain/ports/menus.repository";
import {MenuResponseDto} from "../../domain/models/menu-response-dto.interface";
import {
  mapToEditMenuRequestDto,
  mapToMenuDetail,
  mapToMenuRequestDto,
  mapToMenuSummary
} from "../../application/mappers/menu.mapper";
import {MenuRequestDto} from "../../domain/models/menu-request-dto.interface";
import {Menu} from "../../domain/models/menu.interface";
import {MenuDetailResponseDto} from "../../domain/models/menu-detail-response-dto.interface";
import {environment} from "../../../../../environments/environment.dev";

@Injectable({
  providedIn: 'root'
})
export class MenusAdapter implements MenusRepository {

  private readonly http: HttpClient = inject(HttpClient);
  private readonly baseUrl: string = environment.API_URL;


  getAllMenus(params: { page: number; size: number; }): Observable<Page<Menu>> {
    const requestParams = new HttpParams()
      .set('page', params.page)
      .set('size', params.size);
    return this.http.get<ApiGenericResponse<Page<MenuResponseDto>>>(`${this.baseUrl}/menus`,
      {
        params: requestParams
      }
    ).pipe(
      map((response: ApiGenericResponse<Page<MenuResponseDto>>) => {
        const mappedContent = response.data.content.map(dto => mapToMenuSummary(dto));
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

  getMenuById(uuid: string): Observable<Menu> {
    return this.http.get<ApiGenericResponse<MenuDetailResponseDto>>(`${this.baseUrl}/menus/${uuid}`,
    ).pipe(
      map((response: ApiGenericResponse<MenuDetailResponseDto>) => mapToMenuDetail(response.data)),
      catchError((error: HttpErrorResponse) => {
        throw new Error(error.error.message);
      })
    )
  }

  createMenu(menu: Menu): Observable<string> {
    const mappedMenu: MenuRequestDto = mapToMenuRequestDto(menu);
    return this.http.post<ApiGenericResponse<string>>(`${this.baseUrl}/menus`, mappedMenu
    ).pipe(
      map((response: ApiGenericResponse<string>) => response.data),
      catchError((error: HttpErrorResponse) => {
        throw new Error(error.error.message);
      })
    )
  }

  editMenu(uuid: string, menu: Menu): Observable<Menu> {
    const mappedMenu: MenuRequestDto = mapToEditMenuRequestDto(menu);
    return this.http.put<ApiGenericResponse<MenuDetailResponseDto>>(`${this.baseUrl}/menus/${uuid}`, mappedMenu
    ).pipe(
      map((response: ApiGenericResponse<MenuDetailResponseDto>) => response.data),
      catchError((error: HttpErrorResponse) => {
        throw new Error(error.error.message);
      })
    )
  }

}
