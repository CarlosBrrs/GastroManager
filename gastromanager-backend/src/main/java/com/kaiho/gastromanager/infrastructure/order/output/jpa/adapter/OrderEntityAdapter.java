package com.kaiho.gastromanager.infrastructure.order.output.jpa.adapter;

import com.kaiho.gastromanager.domain.order.exception.OrderDoesNotExistException;
import com.kaiho.gastromanager.domain.order.model.Order;
import com.kaiho.gastromanager.domain.order.spi.OrderPersistencePort;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.criteria.OrderSearchCriteria;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.entity.OrderEntity;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.mapper.OrderEntityMapper;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.repository.OrderEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static com.kaiho.gastromanager.infrastructure.order.output.jpa.specification.OrderEntitySpecification.buildSpecification;

@RequiredArgsConstructor
@Repository
public class OrderEntityAdapter implements OrderPersistencePort {

    private final OrderEntityRepository orderEntityRepository;
    private final OrderEntityMapper orderEntityMapper;

    @Override
    public Page<Order> findAllOrders(OrderSearchCriteria criteria, Pageable pageable) {
        Specification<OrderEntity> spec = buildSpecification(criteria);
        Page<OrderEntity> entityList = orderEntityRepository.findAll(spec, pageable);
        return entityList.map(orderEntityMapper::toDomain);
    }

    @Override
    public Order createOrder(Order order) {

        OrderEntity orderEntity = orderEntityMapper.toEntity(order);

        // guarda la entidad en la base de datos
        OrderEntity savedEntity = orderEntityRepository.save(orderEntity);

        // devuelve el dominio mapeado desde la entidad guardada
        return orderEntityMapper.toDomain(savedEntity);
//        return null;
/*        List<ProductItemEntity> productItemEntityList = productItemRepository.findByUuidIn(order.getOrderItems()
                                                                                                .stream()
                                                                                                .map(orderItem -> orderItem.getProductItem().getUuid())
                                                                                                .toList());

        UserEntity userEntity = userEntityRepository.findById(order.getUser().getUuid()).orElseThrow(() -> new UsernameNotFoundException(order.getUser().getUuid().toString()));

        RestaurantEntity restaurantEntity = restaurantEntityRepository.findById(order.getRestaurant().getUuid())
                                                                      .orElseThrow(() -> new RestaurantDoesNotExistException(order.getRestaurant().getUuid().toString()));

        OrderEntity orderEntity = orderEntityMapper.toEntity(order);

        orderEntity.getOrderItems().forEach(orderItemEntity -> {
            ProductItemEntity productItemEntity = productItemEntityList.stream()
                                                                       .filter(p -> p.getUuid().equals(orderItemEntity.getProductItem().getUuid())).findFirst()
                                                                       .orElseThrow(() -> new ProductItemDoesNotExistException(orderItemEntity.getProductItem().getUuid()));
            productItemEntity.addOrderItem(orderItemEntity);
        });

        userEntity.addOrder(orderEntity);
        restaurantEntity.addOrder(orderEntity);

        OrderEntity savedEntity = orderEntityRepository.save(orderEntity);

        return orderEntityMapper.toDomain(savedEntity);*/
    }

    @Override
    public boolean existsOrderByOrderCode(String base36) {
        return orderEntityRepository.existsByCode(base36);
    }

    @Override
    public Optional<Order> getOrderByUuid(UUID orderUuid, UUID restaurantUuid) {
        return orderEntityRepository.findById(orderUuid, restaurantUuid).map(orderEntityMapper::toDomain);
    }

    @Override
    public UUID changeOrderStatus(UUID orderUuid, String newStatus, String reason) {
        return null;
    }

    @Override
    public Optional<Order> findOrderByUuid(UUID orderUuid, UUID restaurantUuid) {
        return orderEntityRepository.findById(orderUuid, restaurantUuid).map(orderEntityMapper::toDomain);
    }

    @Override
    public void updateOrder(Order order) {
        // Obtener la entidad existente para preservar las colecciones
        OrderEntity existingEntity = orderEntityRepository.findById(order.getUuid())
                .orElseThrow(() -> new OrderDoesNotExistException(order.getUuid()));

        // Actualizar solo los campos necesarios sin tocar las colecciones
        existingEntity.setTotalPaid(order.getTotalPaid());
        existingEntity.setTotalAmount(order.getTotalAmount());
        existingEntity.setOperationalStatus(order.getOperationalStatus());
        existingEntity.setPaymentStatus(order.getPaymentStatus());
        existingEntity.setInvoicingStatus(order.getInvoicingStatus());
        existingEntity.setCustomerNotes(order.getCustomerNotes());
        existingEntity.setTableNumber(order.getTableNumber());
        existingEntity.setCustomerName(order.getCustomerName());
        existingEntity.setRequiresPaymentBeforeOrder(order.isRequiresPaymentBefore());

        orderEntityRepository.save(existingEntity);
    }
}
