import {patchState, signalStore, withHooks, withMethods, withState} from "@ngrx/signals";
import {ColumnProperties} from "../inventory/inventory.store";
import {inject} from "@angular/core";
import {GetMenusUseCase} from "../../../features/menus/application/usecases/get-menus.use-case";
import {rxMethod} from "@ngrx/signals/rxjs-interop";
import {paginationParams} from "../../model/interfaces/pagination/pagination-params.interface";
import {catchError, concatMap, finalize, Observable, of, pipe, switchMap, tap, throwError} from "rxjs";
import {tapResponse} from "@ngrx/operators";
import {HttpErrorResponse} from "@angular/common/http";
import {Menu} from "../../../features/menus/domain/models/menu.interface";
import {Page} from "../../model/interfaces/pagination/page.interface";
import {CreateMenuUseCase} from "../../../features/menus/application/usecases/create-menu.use-case";
import {GetMenuByUuidUseCase} from "../../../features/menus/application/usecases/get-menu-by-uuid.use-case";
import {EditMenuUseCase} from "../../../features/menus/application/usecases/edit-menu.use-case";


type MenuState = {
  pages: Map<number, Menu[]>;
  currentPage: number;
  totalRecords: number;
  tableColumns: Array<ColumnProperties>;
  loading: boolean;
  error: string | null;
  menuToEdit: Menu | null;
  selectedMenu: Menu | null;
}

const initialState: MenuState = {
  pages: new Map<number, Menu[]>(),
  currentPage: 0,
  totalRecords: 0,
  tableColumns: [
    {field: 'name', header: 'Nombre'},
    {field: 'description', header: 'Descripción'},
    // {field: 'updatedDate', header: 'Fecha de Actualización'}
  ],
  selectedMenu: null,
  loading: false,
  error: null,
  menuToEdit: null

};
export const MenusStore = signalStore(
  {providedIn: "root"},
  withState(initialState),
  withMethods((store,
               getMenus = inject(GetMenusUseCase),
               createMenu = inject(CreateMenuUseCase),
               getMenuByUuid = inject(GetMenuByUuidUseCase),
               editMenu = inject(EditMenuUseCase)) => ({
    setSelectedMenu: (menu: Menu) => {
      patchState(store, {selectedMenu: menu});
    },
    getMenus: rxMethod<paginationParams>(
      pipe(
        tap(() => {
          patchState(store, {loading: true, error: null});
        }),
        switchMap((params) => {
          const page = params.page;
          if (store.pages().has(page)) {
            patchState(store, {currentPage: page, loading: false, error: null});
            return of(store.pages().get(page));
          }
          return getMenus.execute(params).pipe(
            tapResponse({
              next: (response) => {
                const {number, content} = response;
                const updatedPages = new Map(store.pages());
                updatedPages.set(number, content);
                patchState(store, {pages: updatedPages, currentPage: page, totalRecords: response.totalElements});
              },
              error: (error: HttpErrorResponse) => {
                const message = error.message || 'Error desconocido';
                patchState(store, {error: message});
              },
              finalize: () => {
                patchState(store, {loading: false})
              }
            })
          )
        })
      )),
    createMenu: (menu: Menu): Observable<Page<Menu>> => {
      patchState(store, {loading: true, error: null});
      return createMenu.execute(menu).pipe(
        tap(createdMenuUuid => {
          console.log('Menu created with uuid:', createdMenuUuid);
        }),
        catchError((error: HttpErrorResponse) => {
          const message = error.message || 'Error desconocido';
          patchState(store, {error: message});
          return throwError(() => error);
        }),
        concatMap((createdMenuUuid) => {
          console.log('New menu with UUID added to the list, reloading...:', createdMenuUuid);
          let params: paginationParams = {page: 0, size: 7};
          return getMenus.execute(params).pipe(
            tapResponse({
              next: (response) => {
                const {number, content} = response;
                const updatedPages = new Map(store.pages());
                updatedPages.set(number, content);
                patchState(store, {
                  pages: updatedPages,
                  currentPage: params.page,
                  totalRecords: response.totalElements
                });
              },
              error: (error: HttpErrorResponse) => {
                const message = error.message || 'Error desconocido';
                patchState(store, {error: message});
              },
            })
          )
        }),
        finalize(() => {
          patchState(store, {loading: false});
        })
      )
    },
    getMenuById: rxMethod<{ uuid: string, forEdit?: boolean }>(
      pipe(
        tap(() => {
          patchState(store, {loading: true, error: null});
        }),
        switchMap(({uuid, forEdit}) => {
          console.log('Fetching menu with UUID:', uuid);
          return getMenuByUuid.execute(uuid).pipe(
            tapResponse({
              next: (response) => {
                console.log('Menu fetched successfully:', response);
                if (forEdit) {
                  console.log('Setting menu to edit:', response);
                  patchState(store, {menuToEdit: response});
                } else {
                  patchState(store, {selectedMenu: response});
                }
              },
              error: (error: HttpErrorResponse) => {
                const message = error.message || 'Error desconocido';
                patchState(store, {error: message});
              },
              finalize: () => {
                patchState(store, {loading: false})
              }
            })
          )
        })
      )
    ),
    clearMenuToEdit: () => patchState(store, {menuToEdit: null}),
    clearSelectedMenu: () => patchState(store, {selectedMenu: null}),
    editMenu: (menu: Menu): Observable<Page<Menu>> => {
      patchState(store, {loading: true, error: null});
      return editMenu.execute(menu.uuid, menu).pipe(
        tap(updatedMenu => {
          console.log('Menu updated with info:', updatedMenu);
        }),
        catchError((error: HttpErrorResponse) => {
          const message = error.message || 'Error desconocido';
          patchState(store, {error: message});
          return throwError(() => error);
        }),
        concatMap((updatedMenu) => {
          console.log('Menu with UUID updated, reloading...:', updatedMenu.uuid);
          let params: paginationParams = {page: 0, size: 7};
          return getMenus.execute(params).pipe(
            tapResponse({
              next: (response) => {
                const {number, content} = response;
                const updatedPages = new Map(store.pages());
                updatedPages.set(number, content);
                patchState(store, {
                  pages: updatedPages,
                  currentPage: params.page,
                  totalRecords: response.totalElements
                });
              },
              error: (error: HttpErrorResponse) => {
                const message = error.message || 'Error desconocido';
                patchState(store, {error: message});
              },
              finalize: () => {
                patchState(store, {loading: false})
              }
            })
          )
        }),
        finalize(() => {
          patchState(store, {loading: false});
        })
      )
    },
  })),
  withHooks({
    onInit: (store) => {
      store.getMenus({page: 0, size: 7});
    }
  })
)
