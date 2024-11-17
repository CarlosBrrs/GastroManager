import {Component, EventEmitter, Input, Output} from '@angular/core';
import {ProductItem} from "../../../core/model/interfaces/ProductItem";
import {CurrencyPipe} from "@angular/common";

@Component({
  selector: 'gm-menu-item-card',
  standalone: true,
  imports: [
    CurrencyPipe
  ],
  templateUrl: './menu-item-card.component.html',
  styleUrl: './menu-item-card.component.scss'
})
export class MenuItemCardComponent {

  @Input() product!: ProductItem;
  @Output() addToOrder = new EventEmitter<ProductItem>();

  onProductClick() {
    if (this.product.isEnabled) {
      this.addToOrder.emit(this.product);
    }
  }

}
