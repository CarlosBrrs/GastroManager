export interface Product {
  uuid: string;
  name: string;
  salePrice: number;
  category: string;
  description?: string;
}

export interface OrderItem {
  product: Product;
  quantity: number;
  subtotal: number;
}

