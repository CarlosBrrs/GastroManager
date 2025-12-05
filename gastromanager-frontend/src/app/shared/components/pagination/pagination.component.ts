import {ChangeDetectionStrategy, Component} from '@angular/core';
import {PaginatorModule, PaginatorState} from "primeng/paginator";

@Component({
  selector: 'gm-pagination',
  standalone: true,
  imports: [
    PaginatorModule
  ],
  templateUrl: './pagination.component.html',
  styleUrl: './pagination.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PaginationComponent {

  changePageHandler($event: PaginatorState) {
    console.log($event);
  }
}
