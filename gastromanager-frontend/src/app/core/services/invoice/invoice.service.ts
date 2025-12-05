import {Injectable} from '@angular/core';
import {BaseHttpService} from "../basehttp/base-http.service";
import {Observable} from "rxjs";
import {ProductItemRequestDto} from "../../model/interfaces/ProductItemRequestDto";
import {Invoice} from '../../store/invoice/invoice.model';

@Injectable({
  providedIn: 'root'
})
export class InvoiceService extends BaseHttpService {

  getAllInvoices(): Observable<Invoice[]> {
    return this.handleRequest<Invoice[]>("GET", "invoices");
  }

  addProductItem(productItem: ProductItemRequestDto): Observable<string> {
    return this.handleRequest<string>("POST", "product-items", productItem);
  }


  deleteProductItem(productItemUuid: string): void {
    alert("not implemented yet");
  }
}
