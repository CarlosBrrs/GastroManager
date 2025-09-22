export interface OrderItemRequestDto {
  productUuid: string;
  quantity: number;
  customerNotes?: string;
}
