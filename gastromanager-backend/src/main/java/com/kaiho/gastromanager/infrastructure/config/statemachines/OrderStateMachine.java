package com.kaiho.gastromanager.infrastructure.config.statemachines;

import com.kaiho.gastromanager.domain.order.model.OrderEvent;
import com.kaiho.gastromanager.domain.order.model.OrderStatus;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import static com.kaiho.gastromanager.domain.order.model.OrderStatus.AWAITING_PAYMENT;
import static com.kaiho.gastromanager.domain.order.model.OrderStatus.CANCELLED;
import static com.kaiho.gastromanager.domain.order.model.OrderStatus.COMPLETED;
import static com.kaiho.gastromanager.domain.order.model.OrderStatus.PENDING;
import static com.kaiho.gastromanager.domain.order.model.OrderStatus.PREPARING;
import static com.kaiho.gastromanager.domain.order.model.OrderStatus.READY;
import static com.kaiho.gastromanager.domain.order.model.OrderStatus.SERVED;

@Configuration
@EnableStateMachine
public class OrderStateMachine extends StateMachineConfigurerAdapter<OrderStatus, OrderEvent> {

    @Override
    public void configure(StateMachineStateConfigurer<OrderStatus, OrderEvent> states) throws Exception {
        states
                .withStates()
                .initial(AWAITING_PAYMENT) // Cambiar dinámicamente si es necesario
                .state(PENDING)
                .state(PREPARING)
                .state(READY)
                .state(SERVED)
                .state(COMPLETED)
                .end(CANCELLED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStatus, OrderEvent> transitions) throws Exception {
        transitions.withExternal()
                .source(OrderStatus.AWAITING_PAYMENT).target(OrderStatus.PENDING)
                .event(OrderEvent.PAYMENT_COMPLETED)   // Si el pago es recibido, pasa a Pending
                .and().withExternal()
                .source(OrderStatus.PENDING).target(OrderStatus.PREPARING)
                .event(OrderEvent.START_PREPARATION)  // Cuando cocina comienza, pasa a Preparing
                .and().withExternal()
                .source(OrderStatus.PREPARING).target(OrderStatus.READY)
                .event(OrderEvent.ORDER_READY)      // Cuando la orden está lista, pasa a Ready
                .and().withExternal()
                .source(OrderStatus.READY).target(OrderStatus.SERVED)
                .event(OrderEvent.ORDER_SERVED)     // Cuando la orden es servida, pasa a Served
                .and().withExternal()
                .source(OrderStatus.SERVED).target(OrderStatus.COMPLETED)
                .event(OrderEvent.COMPLETE_ORDER);  // Cuando la orden se completa, pasa a Completed

    }
}
