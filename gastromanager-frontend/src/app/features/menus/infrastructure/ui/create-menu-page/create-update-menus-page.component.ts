import {ChangeDetectionStrategy, Component, computed, inject, OnInit, Signal} from '@angular/core';
import {
  CreateIngredientComponent
} from "../../../../ingredients/infrastructure/ui/create-ingredient/create-ingredient.component";
import {MenusStore} from "../../../../../core/store/menus/menus.store";
import {Menu} from "../../../domain/models/menu.interface";
import {CreateMenuComponent} from "../create-menu/create-menu.component";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'gm-create-update-menus-page',
  standalone: true,
  imports: [
    CreateIngredientComponent,
    CreateMenuComponent
  ],
  templateUrl: './create-update-menus-page.component.html',
  styleUrl: './create-update-menus-page.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CreateUpdateMenusPageComponent implements OnInit {

  private readonly menusStore = inject(MenusStore);
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
  }
}
