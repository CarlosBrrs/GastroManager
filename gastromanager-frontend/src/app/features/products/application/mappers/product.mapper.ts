import {Product} from "../../domain/models/product.interface";
import {ProductRequestDto} from "../../domain/models/product-request-dto.interface";
import {ProductSummaryResponseDto} from "../../domain/models/product-summary-response-dto.interface";
import {
  ProductCategoryItemDto,
  ProductGroupByResponseDto
} from "../../domain/models/product-group-by-response-dto.interface";
import {ProductsByCategory} from "../../domain/models/products-by-category.interface";

export function mapToProductRequestDto(product: Product): ProductRequestDto {
  // Verificar si es modo avanzado (tiene recipes o ingredients)
  const isAdvancedMode = (product.recipes && product.recipes.length > 0) ||
    (product.ingredients && product.ingredients.length > 0);

  if (isAdvancedMode) {
    // Modo avanzado: no enviar purchasePrice
    return {
      name: product.name,
      description: product.description,
      category: product.category,
      salePrice: product.salePrice,
      recipes: product.recipes || [],
      ingredients: product.ingredients || []
    };
  } else {
    // Modo básico: enviar purchasePrice, sin recipes ni ingredients
    return {
      name: product.name,
      description: product.description,
      category: product.category,
      purchasePrice: product.purchasePrice,
      salePrice: product.salePrice
    };
  }
}

export function mapToProduct(product: ProductSummaryResponseDto): Product {
  return {
    uuid: product.uuid,
    name: product.name,
    description: product.description,
    salePrice: product.salePrice,
    category: 'mocked-category',
    purchasePrice: product.purchasePrice,
    createdBy: 'MOCKED',
    createdDate: new Date(),
    updatedBy: 'MOCKED',
    updatedDate: new Date()
  };
}

export function mapToProductsByCategory(dto: ProductGroupByResponseDto): ProductsByCategory {
  const result: ProductsByCategory = {};

  dto.categories.forEach(category => {
    result[category.name] = category.products.map((item: ProductCategoryItemDto) => ({
      uuid: item.id,
      name: item.name,
      description: item.description,
      salePrice: item.price,
      purchasePrice: undefined,
      category: item.category,
      createdBy: 'SYSTEM',
      createdDate: new Date(),
      updatedBy: 'SYSTEM',
      updatedDate: new Date()
    }));
  });

  return result;
}
