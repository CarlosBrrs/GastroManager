package com.kaiho.gastromanager.infrastructure.order.output.jpa.adapter;

import com.kaiho.gastromanager.domain.order.model.Order;
import com.kaiho.gastromanager.domain.order.spi.OrderPersistencePort;
import com.kaiho.gastromanager.domain.orderitem.model.OrderItem;
import com.kaiho.gastromanager.domain.productitem.exception.ProductItemDoesNotExistException;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.entity.OrderEntity;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.mapper.OrderEntityMapper;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.repository.OrderEntityRepository;
import com.kaiho.gastromanager.infrastructure.orderitem.output.jpa.entity.OrderItemEntity;
import com.kaiho.gastromanager.infrastructure.orderitem.output.jpa.mapper.OrderItemEntityMapper;
import com.kaiho.gastromanager.infrastructure.productitem.output.jpa.entity.ProductItemEntity;
import com.kaiho.gastromanager.infrastructure.productitem.output.jpa.repository.ProductItemRepository;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.entity.UserEntity;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderEntityAdapter implements OrderPersistencePort {

    private final OrderEntityRepository orderEntityRepository;
    private final OrderEntityMapper orderEntityMapper;
    private final UserEntityRepository userEntityRepository;
    private final OrderItemEntityMapper orderItemEntityMapper;
    private final ProductItemRepository productItemRepository;

    @Override
    public List<Order> findAllOrders() {
        return orderEntityRepository.findAll().stream().map(orderEntityMapper::toDomain).toList();
    }

    @Override
    public Order createOrder(Order order) {

        // todos los productitem relacionados de cada orderitem
        List<ProductItemEntity> productItemEntityList = productItemRepository.findByUuidIn(order.orderItems().stream()
                .map(OrderItem::productItemUuid)
                .toList());

        // el user de la order
        UserEntity userEntity = userEntityRepository.findById(order.userUuid())
                .orElseThrow(() -> new UsernameNotFoundException(order.userUuid().toString()));

        // la order
        OrderEntity orderEntity = orderEntityMapper.toEntity(order);

        // Limpia los OrderItems actuales si los hubiera
        orderEntity.getOrderItems().clear();

        //para cada orderitementity hay que setear la order y el productitem
        List<OrderItemEntity> orderItemEntityList = order.orderItems().stream()
                .map(orderItem ->
                        {
                            //map the order to orderitem
                            OrderItemEntity orderItemEntity = orderItemEntityMapper.toEntity(orderItem);

                            //find the productitem that corresponds to the productitemuuid in the oderitem
                            ProductItemEntity productItemEntity = productItemEntityList.stream()
                                    .filter(p -> p.getUuid().equals(orderItem.productItemUuid()))
                                    .findFirst()
                                    .orElseThrow(() -> new ProductItemDoesNotExistException(orderItem.productItemUuid()));

                            //set the found productitem to the orderitem
                            productItemEntity.addOrderItem(orderItemEntity);
orderEntity.addOrderItem(orderItemEntity);
                            return orderItemEntity;
                        }
                )
                .toList();

        //associate each orderitem to the order
        orderItemEntityList.forEach(orderEntity::addOrderItem);

        // the order is added to the user´s orders and the order is setted with this user
        userEntity.addOrder(orderEntity);

        //TODO: after saving the order we have to update the inventory of the ingredient. Not implemented yet
        return orderEntityMapper.toDomain(orderEntityRepository.save(orderEntity));
    }
}
