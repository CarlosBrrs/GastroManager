import {patchState, signalStore, withHooks, withMethods, withState} from "@ngrx/signals";
import {ColumnProperties} from "../inventory/inventory.store";
import {inject} from "@angular/core";
import {rxMethod} from "@ngrx/signals/rxjs-interop";
import {catchError, finalize, Observable, pipe, switchMap, tap, throwError} from "rxjs";
import {tapResponse} from "@ngrx/operators";
import {HttpErrorResponse} from "@angular/common/http";
import {Submenu} from "../../../features/submenus/domain/models/submenu.interface";
import {
  GetSubmenusByParamsUseCase
} from "../../../features/submenus/application/usecases/get-submenus-by-params.use-case";
import {CreateSubmenuUseCase} from "../../../features/submenus/application/usecases/create-submenu.use-case";


type SubmenuState = {
  submenusByMenu: Map<string, Map<number, Submenu[]>>;
  currentPage: number;
  totalRecords: number;
  tableColumns: Array<ColumnProperties>;
  loading: boolean;
  error: string | null;
}

const initialState: SubmenuState = {
  submenusByMenu: new Map(),
  currentPage: 0,
  totalRecords: 0,
  tableColumns: [
    {field: 'name', header: 'Nombre'},
    {field: 'description', header: 'Descripción'},
    // {field: 'updatedDate', header: 'Fecha de Actualización'}
  ],
  loading: false,
  error: null,

};
export const SubmenuStore = signalStore(
  {providedIn: "root"},
  withState(initialState),
  withMethods((store,
               getSubmenuByParamsUseCase = inject(GetSubmenusByParamsUseCase),
               createSubmenu = inject(CreateSubmenuUseCase)
  ) => ({
    getSubmenus: rxMethod<{ search: string, menuUuid: string, page: number, size: number }>(
      pipe(
        tap(() => {
          patchState(store, {loading: true, error: null});
        }),
        switchMap((params) => {
          return getSubmenuByParamsUseCase.execute(params).pipe(
            tapResponse({
              next: (response) => {
                console.log('Submenus fetched successfully:', response);
                const menuUuid = params.menuUuid;
                const page = response.number;
                const submenus = response.content;

                // Clona el mapa actual
                const updatedSubmenusByMenu = new Map(store.submenusByMenu());
                // Obtiene o crea el mapa de páginas para el menú
                const pagesMap = updatedSubmenusByMenu.get(menuUuid) ?? new Map<number, Submenu[]>();
                // Inserta los submenús en la página correspondiente
                pagesMap.set(page, submenus);
                updatedSubmenusByMenu.set(menuUuid, pagesMap);

                patchState(store, {
                  submenusByMenu: updatedSubmenusByMenu,
                  currentPage: page,
                  totalRecords: response.totalElements
                });
                console.log('store', store.submenusByMenu());
                console.log('store', store.currentPage());
                console.log('store', store.totalRecords());
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
    createSubmenu: (submenu: Submenu): Observable<string> => {
      // createSubmenu: (submenu: Submenu): Observable<Page<Submenu>> => {
      patchState(store, {loading: true, error: null});
      return createSubmenu.execute(submenu).pipe(
        tapResponse({
          next: (response) => {
            console.log('Submenu created with UUID:', response);
            patchState(store, {error: null});
          },
          error: (error: HttpErrorResponse) => {
            const message = error.message || 'Error desconocido';
            patchState(store, {error: message});
          },
        }),
        catchError((error: HttpErrorResponse) => {
          const message = error.message || 'Error desconocido';
          patchState(store, {error: message});
          return throwError(() => error);
        }),
        finalize(() => {
          patchState(store, {loading: false});
        })
      )
    },


    /*    getMenus: rxMethod<paginationParams>(
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
        },*/
  })),
  withHooks({
    onInit: (store) => {
    }
  })
)
