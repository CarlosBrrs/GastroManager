import {ChangeDetectionStrategy, Component, computed, EventEmitter, input, Output, signal} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Product} from '../../../../products/domain/models/product.interface';
import {ProductFilterComponent} from '../product-filter/product-filter.component';
import {ProductsByCategory} from "../../../../products/domain/models/products-by-category.interface";

@Component({
  selector: 'gm-product-selector',
  standalone: true,
  imports: [CommonModule, ProductFilterComponent],
  templateUrl: './product-selector.component.html',
  styleUrl: './product-selector.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ProductSelectorComponent {

  productsByCategory = input<ProductsByCategory>({});
  @Output() productSelected = new EventEmitter<Product>();
  searchTerm = signal('');
  expandedCategories = signal<Set<string>>(new Set());
  filteredProductsByCategory = computed((): ProductsByCategory => {
    const products = this.productsByCategory();

    if (!products || Object.keys(products).length === 0) {
      return {};
    }

    const search = this.searchTerm().toLowerCase().trim();

    if (!search) {
      return products;
    }

    const filtered: ProductsByCategory = {};

    Object.entries(products).forEach(([categoryName, productList]) => {
      const matchingProducts = productList.filter(product =>
        product.name.toLowerCase().includes(search) ||
        product.description.toLowerCase().includes(search) ||
        product.category.toLowerCase().includes(search)
      );

      if (matchingProducts.length > 0) {
        filtered[categoryName] = matchingProducts;
      }
    });

    return filtered;
  });

  getCategoryNames(): string[] {
    const filtered = this.filteredProductsByCategory();
    return Object.keys(filtered);
  }

  getProductsForCategory(categoryName: string): Product[] {
    const filtered = this.filteredProductsByCategory();
    return filtered[categoryName] || [];
  }

  isCategoryExpanded(categoryName: string): boolean {
    return this.expandedCategories().has(categoryName);
  }

  toggleCategory(categoryName: string): void {
    const expanded = new Set(this.expandedCategories());

    if (expanded.has(categoryName)) {
      expanded.delete(categoryName);
    } else {
      expanded.add(categoryName);
    }

    this.expandedCategories.set(expanded);
  }

  onSearchChanged(searchTerm: string): void {
    this.searchTerm.set(searchTerm);
  }

  selectProduct(product: Product): void {
    this.productSelected.emit(product);
  }

  highlightSearchTerm(text: string): string {
    const searchTerm = this.searchTerm().toLowerCase().trim();

    if (!searchTerm || !text) {
      return text;
    }

    // Escapar caracteres especiales para regex
    const escapedSearchTerm = searchTerm.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
    const regex = new RegExp(`(${escapedSearchTerm})`, 'gi');

    return text.replace(regex, '<mark class="search-highlight">$1</mark>');
  }

}
