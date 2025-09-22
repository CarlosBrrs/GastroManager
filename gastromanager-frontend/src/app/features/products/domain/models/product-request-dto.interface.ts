export interface ProductRequestDto {
  name: string;
  description: string;
  salePrice: number;
  purchasePrice?: number;
  category: string;
  createdBy: string;
  createdDate: Date;
  updatedBy: string;
  updatedDate: Date;
}
