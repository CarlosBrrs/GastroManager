export interface InvoiceRequestDto {
  customerName: string;
  customerPhone: string;
  customerEmail: string;
  invoiceItems: InvoiceItem[];
  tipAmount: number;
}

export interface InvoiceItem {
  orderItemUuid: string;
  quantity: number;
}
