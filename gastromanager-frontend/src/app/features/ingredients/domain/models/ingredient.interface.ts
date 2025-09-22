export interface Ingredient {
  uuid: string;
  name: string;
  supplier: string;
  unit: string;
  availableStock: number;
  pricePerUnit: number;
  minimumStockQuantity: number;
  createdBy: string;
  createdDate: Date;
  updatedBy: string;
  updatedDate: Date;
}
