import {ChangeDetectionStrategy, Component, EventEmitter, inject, Input, Output} from '@angular/core';
import {FormGroup, NonNullableFormBuilder, ReactiveFormsModule, Validators} from "@angular/forms";
import {ProgressSpinnerModule} from "primeng/progressspinner";
import {Menu} from "../../../domain/models/menu.interface";

@Component({
  selector: 'gm-create-menu',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    ProgressSpinnerModule
  ],
  templateUrl: './create-menu.component.html',
  styleUrl: './create-menu.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CreateMenuComponent {

  fb = inject(NonNullableFormBuilder)
  menuForm: FormGroup = this.fb.group({
    uuid: this.fb.control('', []),
    name: this.fb.control("", [Validators.required, Validators.minLength(3)]),
    description: this.fb.control("", [Validators.required, Validators.minLength(3)]),
  });
  @Input() loading: boolean = false;
  @Output() onEditMenu = new EventEmitter<Menu>();
  @Input() error: string | null = null;
  @Output() onCreateMenu = new EventEmitter<Menu>();

  private _menuToEdit?: Menu;

  get menuToEdit(): Menu | undefined {
    return this._menuToEdit;
  }

  @Input()
  set menuToEdit(menu: Menu | undefined) {
    this._menuToEdit = menu;
    if (menu) {
      console.log('Setting menu to edit:', menu);
      this.menuForm.patchValue({
        ...menu
      });
    } else {
      this.menuForm.reset();
    }
  }

  onSubmit() {
    if (this.menuForm.valid) {
      const menu = this.menuForm.value as Menu;

      if (this.menuToEdit) {
        this.onEditMenu.emit(menu);
      } else {
        this.onCreateMenu.emit(menu)
      }
    }
  }
}
