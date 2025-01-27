package com.kaiho.gastromanager.infrastructure.order.output.jpa.adapter;

import com.kaiho.gastromanager.domain.order.model.Order;
import com.kaiho.gastromanager.domain.order.spi.OrderPersistencePort;
import com.kaiho.gastromanager.domain.productitem.exception.ProductItemDoesNotExistException;
import com.kaiho.gastromanager.domain.restaurant.exception.RestaurantDoesNotExistException;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.entity.OrderEntity;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.mapper.OrderEntityMapper;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.repository.OrderEntityRepository;
import com.kaiho.gastromanager.infrastructure.productitem.output.jpa.entity.ProductItemEntity;
import com.kaiho.gastromanager.infrastructure.productitem.output.jpa.repository.ProductItemRepository;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.repository.RestaurantEntityRepository;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.entity.UserEntity;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class OrderEntityAdapter implements OrderPersistencePort {

    private final OrderEntityRepository orderEntityRepository;
    private final OrderEntityMapper orderEntityMapper;
    private final UserEntityRepository userEntityRepository;
    private final ProductItemRepository productItemRepository;
    private final RestaurantEntityRepository restaurantEntityRepository;

    @Override
    public List<Order> findAllOrders() {
        return orderEntityRepository.findAll().stream().map(orderEntityMapper::toDomain).toList();
    }

    @Override
    public Order createOrder(Order order) {

        List<ProductItemEntity> productItemEntityList = productItemRepository.findByUuidIn(order.getOrderItems()
                .stream()
                .map(orderItem -> orderItem.getProductItem().getUuid())
                .toList());

        UserEntity userEntity = userEntityRepository.findById(order.getUser().uuid()).orElseThrow(() -> new UsernameNotFoundException(order.getUser().uuid().toString()));

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

        return orderEntityMapper.toDomain(savedEntity);
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
        Optional<OrderEntity> orderEntity = orderEntityRepository.findById(orderUuid, restaurantUuid);
        return orderEntity
                .map(orderEntityMapper::toDomain);
    }
}
