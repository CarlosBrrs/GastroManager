import {Unit} from "../../model/interfaces/UnitType";

export type IngredientItem = {
  uuid: string;
  name: string;
  availableStock: number;
  supplier: string;
  category: string;
  unit: Unit;
  pricePerUnit: number;
  minimumStockQuantity: number;
  isEnabled: boolean;
  createdBy: string;
  createdDate: string;
  updatedBy: string;
  updatedDate: string;
}
