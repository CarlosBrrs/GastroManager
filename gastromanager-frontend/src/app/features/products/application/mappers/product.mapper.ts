import {Product} from "../../domain/models/product.interface";
import {ProductRequestDto} from "../../domain/models/product-request-dto.interface";
import {ProductSummaryResponseDto} from "../../domain/models/product-summary-response-dto.interface";
import {ProductGroupByResponseDto, ProductCategoryItemDto} from "../../domain/models/product-group-by-response-dto.interface";
import {ProductsByCategory} from "../../domain/models/products-by-category.interface";

export function mapToProductRequestDto(product: Product): ProductRequestDto {
  return {
    name: product.name,
    purchasePrice: product.purchasePrice,
    salePrice: product.salePrice,
    category: product.category,
    description: product.description,
    createdBy: product.createdBy,
    createdDate: product.createdDate,
    updatedBy: product.updatedBy,
    updatedDate: product.updatedDate
  };

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
