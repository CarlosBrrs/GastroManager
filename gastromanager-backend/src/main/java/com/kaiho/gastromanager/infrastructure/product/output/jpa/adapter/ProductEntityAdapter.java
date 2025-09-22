package com.kaiho.gastromanager.infrastructure.product.output.jpa.adapter;

import com.kaiho.gastromanager.domain.product.model.CategoryGroup;
import com.kaiho.gastromanager.domain.product.model.Product;
import com.kaiho.gastromanager.domain.product.model.ProductCategoryItem;
import com.kaiho.gastromanager.domain.product.model.ProductGroupByResult;
import com.kaiho.gastromanager.domain.product.spi.ProductPersistencePort;
import com.kaiho.gastromanager.infrastructure.product.output.jpa.criteria.ProductGroupByCriteria;
import com.kaiho.gastromanager.infrastructure.product.output.jpa.criteria.ProductSearchCriteria;
import com.kaiho.gastromanager.infrastructure.product.output.jpa.entity.ProductEntity;
import com.kaiho.gastromanager.infrastructure.product.output.jpa.mapper.ProductCategoryItemMapper;
import com.kaiho.gastromanager.infrastructure.product.output.jpa.mapper.ProductEntityMapper;
import com.kaiho.gastromanager.infrastructure.product.output.jpa.repository.ProductEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.kaiho.gastromanager.infrastructure.product.output.jpa.specification.ProductEntitySpecification.buildGroupBySpecification;
import static com.kaiho.gastromanager.infrastructure.product.output.jpa.specification.ProductEntitySpecification.buildSpecification;

@RequiredArgsConstructor
@Repository
public class ProductEntityAdapter implements ProductPersistencePort {

    private final ProductEntityMapper productEntityMapper;
    private final ProductCategoryItemMapper productCategoryItemMapper;
    private final ProductEntityRepository productEntityRepository;

/*    @Override
    public Page<Menu> getAllMenus(MenuSearchCriteria criteria, Pageable pageable) {
        Specification<MenuEntity> spec = buildSpecification(criteria);
        Page<MenuEntity> entityList = menuEntityRepository.findAll(spec, pageable);
        return entityList.map(menuEntityMapper::toDomain);
    }*/

/*    @Override
    public boolean menuExistsByName(String name, UUID restaurantUuid) {
        return menuEntityRepository.existsByName(name, restaurantUuid);
    }*/

    @Override
    public boolean productExistsByName(String name, UUID restaurantUuid) {
        return productEntityRepository.existsByName(name, restaurantUuid);
    }

    @Override
    public UUID createProduct(Product product) {
        ProductEntity entity = productEntityMapper.toEntity(product);
        ProductEntity saved = productEntityRepository.save(entity);
        return saved.getUuid();
    }

    @Override
    public Page<Product> getAllProducts(ProductSearchCriteria criteria, Pageable pageable) {
        Specification<ProductEntity> spec = buildSpecification(criteria);
        Page<ProductEntity> entityList = productEntityRepository.findAll(spec, pageable);
        return entityList.map(productEntityMapper::toDomain);
    }

    @Override
    public List<Product> findAllByUuidInAndRestaurant(List<UUID> productUuids, UUID restaurantUuid) {
        List<ProductEntity> productEntities = productEntityRepository.findAllByUuidInAndRestaurant(productUuids, restaurantUuid);

        return productEntities.stream()
                              .map(productEntityMapper::toDomain)
                              .toList();
    }

    @Override
    public ProductGroupByResult getProductsGroupedByCategory(ProductGroupByCriteria criteria, UUID restaurantUuid) {
        // Construir specification para filtrar en BD
        Specification<ProductEntity> spec = buildGroupBySpecification(criteria, restaurantUuid);

        // Obtener productos filtrados directamente desde BD ordenados por categoría y nombre
        List<ProductEntity> filteredProducts = productEntityRepository.findAll(spec, Sort.by("category", "name"));

        // Agrupar por categoría manteniendo el orden
        Map<String, List<ProductEntity>> groupedProducts = filteredProducts.stream()
                .collect(Collectors.groupingBy(
                        product -> product.getCategory() != null ? product.getCategory() : "Sin categoría",
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        // Convertir a modelos de dominio
        List<CategoryGroup> categories = new ArrayList<>();
        int totalProducts = 0;

        for (Map.Entry<String, List<ProductEntity>> entry : groupedProducts.entrySet()) {
            String categoryName = entry.getKey();
            List<ProductEntity> categoryProducts = entry.getValue();

            // Si no incluir vacías y la categoría está vacía, saltar
            if (!criteria.includeEmpty() && categoryProducts.isEmpty()) {
                continue;
            }

            List<ProductCategoryItem> productDomainItems = categoryProducts.stream()
                    .map(productCategoryItemMapper::toDomain)
                    .collect(Collectors.toList());

            categories.add(CategoryGroup.builder()
                    .name(categoryName)
                    .products(productDomainItems)
                    .build());

            totalProducts += categoryProducts.size();
        }

        return ProductGroupByResult.builder()
                .categories(categories)
                .totalProducts(totalProducts)
                .totalCategories(categories.size())
                .build();
    }
}
