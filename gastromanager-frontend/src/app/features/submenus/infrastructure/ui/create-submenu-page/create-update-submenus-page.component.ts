import {ChangeDetectionStrategy, Component, inject, OnInit} from '@angular/core';
import {
  CreateIngredientComponent
} from "../../../../ingredients/infrastructure/ui/create-ingredient/create-ingredient.component";
import {CreateMenuComponent} from "../../../../menus/infrastructure/ui/create-menu/create-menu.component";
import {ActivatedRoute, Router} from "@angular/router";
import {CreateSubmenuComponent} from "../../../../menus/infrastructure/ui/create-submenu/create-submenu.component";
import {Submenu} from "../../../domain/models/submenu.interface";
import {SubmenuStore} from "../../../../../core/store/submenus/submenu.store";

@Component({
  selector: 'gm-create-update-submenus-page',
  standalone: true,
  imports: [
    CreateIngredientComponent,
    CreateMenuComponent,
    CreateSubmenuComponent
  ],
  templateUrl: './create-update-submenus-page.component.html',
  styleUrl: './create-update-submenus-page.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CreateUpdateSubmenusPageComponent implements OnInit {

  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly submenuStore = inject(SubmenuStore);
  menuUuid: string | undefined

  ngOnInit() {
    this.menuUuid = this.route.snapshot.paramMap.get('menuUuid') ?? '';
  }

  /* private readonly menusStore = inject(MenusStore);
   private readonly route = inject(ActivatedRoute);
   private readonly router = inject(Router);

   menuToEdit = this.menusStore.menuToEdit as Signal<Menu | undefined>;
   loading = computed(() => this.menusStore.loading());
   error = computed(() => this.menusStore.error());

   ngOnInit(): void {
     const uuid = this.route.snapshot.paramMap.get('uuid');
     if (uuid) {
       console.log('fetching menu to edit with UUID:', uuid);
       this.menusStore.getMenuById({uuid, forEdit: true});
     } else {
       this.menusStore.clearMenuToEdit();
     }
   }

   createMenuHandler(menu: Menu) {
     this.menusStore.createMenu(menu).subscribe(
       {
         next: () => {
           console.log('Menu added successfully:', menu);
           this.router.navigate(['/menus']);
         },
         error: (error) => {
           console.error('Error editing menu:', error);
           // Optionally, you can show an error message to the user
         },
         complete: () => {
           console.log('Edit operation completed');
           // Optionally, you can navigate back or show a success message
         }
       }
     );
   }

   editMenuHandler(menu: Menu) {
     this.menusStore.editMenu(menu).subscribe(
       {
         next: () => {
           console.log('Menu edited successfully:', menu);
           this.menusStore.clearMenuToEdit();
           this.router.navigate(['/menus']);
         },
         error: (error) => {
           console.error('Error editing ingredient:', error);
           // Optionally, you can show an error message to the user
         },
         complete: () => {
           console.log('Edit operation completed');
           // Optionally, you can navigate back or show a success message
         }
       }
     );
   }*/
  onSubmenuCreateHandler($event: Submenu) {
    this.submenuStore.createSubmenu($event).subscribe({
      next: (res) => {
        console.log("response in subscribe ", res)
        this.router.navigate(['/menus']);
      }
    })
  }
}
