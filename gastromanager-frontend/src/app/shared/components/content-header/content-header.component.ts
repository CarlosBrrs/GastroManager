import {ChangeDetectionStrategy, Component, inject} from '@angular/core';
import {LayoutStore} from "../../../layouts/authenticated-layout/store/authenticated-layout.store";

@Component({
  selector: 'gm-content-header',
  standalone: true,
  imports: [],
  templateUrl: './content-header.component.html',
  styleUrl: './content-header.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ContentHeaderComponent {
  readonly title = inject(LayoutStore).moduleTitle;
}
