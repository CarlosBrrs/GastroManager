import {Component, OnInit, signal} from '@angular/core';
import {ProductItemService} from "../../core/services/product-item/product-item.service";
import {ProductItemResponseDto} from "../../core/model/interfaces/ProductItemResponseDto";
import {InventoryTableComponent} from "../inventory/inventory-table/inventory-table.component";
import {ProductItemTableComponent} from "./product-item-table/product-item-table.component";
import {finalize, Subject, takeUntil} from "rxjs";

@Component({
  selector: 'gm-product-item',
  standalone: true,
  imports: [
    InventoryTableComponent,
    ProductItemTableComponent
  ],
  templateUrl: './product-item.component.html',
  styleUrl: './product-item.component.scss'
})
export class ProductItemComponent implements OnInit {

  productItems = signal<ProductItemResponseDto[]>([]);
  loading = signal<boolean>(false);
  error = signal<string | null>(null);
  private destroy$ = new Subject<void>();

  constructor(private productItemService: ProductItemService) {
  }

  ngOnInit(): void {
    this.loadProductItems();
  }

  handleAdd(productItem: any) {
    this.productItemService.addProductItem(productItem).subscribe({
      next: response => {
        this.loadProductItems();
      }, error: error => {
        console.log("error adding product item")
        this.error.set('Error adding product item');
        window.alert(error.message);
        console.log(error);
      }, complete: ()=> {
        console.log("completed handle add in product item component")
      }});}
/*

      response => {

    }, error => {
      window.alert(error)
    })
  }*/

  handleDelete(productItemUuid: number) {


  }

  handleEdit(productItem: any) {
    this.loading.set(true);
    this.productItemService.updateProductItem(productItem.uuid, productItem.payload)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => this.loading.set(false))
      ).subscribe(response => {
      this.loadProductItems()
      this.loading.set(false);
    })
  }

  private loadProductItems() {
    this.productItemService.getAllProductItems().subscribe(response => {
      this.productItems.set(response.data);
    })
  }
}
