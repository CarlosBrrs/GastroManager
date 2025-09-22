export interface ProductGroupByResponseDto {
  categories: CategoryGroupDto[];
  totalProducts: number;
  totalCategories: number;
}

export interface CategoryGroupDto {
  name: string;
  products: ProductCategoryItemDto[];
}

export interface ProductCategoryItemDto {
  id: string;
  name: string;
  price: number;
  category: string;
  description: string;
  isEnabled: boolean;
}
