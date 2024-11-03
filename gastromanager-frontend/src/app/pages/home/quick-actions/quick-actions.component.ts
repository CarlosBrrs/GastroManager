import {Component, EventEmitter, Input, Output} from '@angular/core';
import {PanelModule} from "primeng/panel";
import {ListboxChangeEvent, ListboxModule} from "primeng/listbox";
import {QuickAction} from "../../../core/model/interfaces/QuickAction";

@Component({
  selector: 'gm-quick-actions',
  standalone: true,
  imports: [
    PanelModule,
    ListboxModule
  ],
  templateUrl: './quick-actions.component.html',
  styleUrl: './quick-actions.component.scss'
})
export class QuickActionsComponent {
  @Input() actions: QuickAction[] = [];
  @Output() execute = new EventEmitter();

  onActionSelect($event: ListboxChangeEvent) {
    this.execute.emit($event.value);
  }
}
