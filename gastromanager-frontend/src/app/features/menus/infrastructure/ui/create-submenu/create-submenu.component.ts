import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  inject,
  Input,
  OnChanges,
  Output,
  SimpleChanges
} from '@angular/core';
import {FormGroup, NonNullableFormBuilder, ReactiveFormsModule, Validators} from "@angular/forms";
import {ProgressSpinnerModule} from "primeng/progressspinner";
import {Submenu} from "../../../../submenus/domain/models/submenu.interface";

@Component({
  selector: 'gm-create-submenu',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    ProgressSpinnerModule
  ],
  templateUrl: './create-submenu.component.html',
  styleUrl: './create-submenu.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CreateSubmenuComponent implements OnChanges {

  @Input() menuUuid!: string | undefined;
  @Output() onSubmenuCreate = new EventEmitter<Submenu>();

  fb = inject(NonNullableFormBuilder);
  submenuForm: FormGroup = this.fb.group({
    uuid: this.fb.control('', []),
    name: this.fb.control("", [Validators.required, Validators.minLength(3)]),
    description: this.fb.control("", [Validators.required, Validators.minLength(3)]),
    menu: this.fb.group({
      uuid: this.fb.control(this.menuUuid ?? '', [Validators.required])
    }),
  });

  ngOnChanges(changes: SimpleChanges) {
    if (changes['menuUuid']) {
      this.submenuForm.get('menu')?.get('uuid')?.setValue(this.menuUuid);
    }
  }

  /*
    menuForm: FormGroup = this.fb.group({
      uuid: this.fb.control('', []),
      name: this.fb.control("", [Validators.required, Validators.minLength(3)]),
      description: this.fb.control("", [Validators.required, Validators.minLength(3)]),
    });
    @Input() loading: boolean = false;
    @Output() onEditMenu = new EventEmitter<Menu>();
    @Input() error: string | null = null;
    @Output() onCreateMenu = new EventEmitter<Menu>();

    get menuToEdit(): Menu | undefined {
      return this._menuToEdit;
    }

    private _menuToEdit?: Menu;
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
    }*/

  onSubmit() {
    const value = this.submenuForm.value as Submenu;
    this.onSubmenuCreate.emit(value);
  }
}
