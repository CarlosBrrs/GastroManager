package com.kaiho.gastromanager.infrastructure.config.context;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
public class RestaurantFilterAspect {

    private final EntityManager entityManager;

    @Pointcut("within(org.springframework.data.jpa.repository.JpaRepository+)")
    public void repositoryMethods() {}

    @Around("repositoryMethods()")
    public Object applyFilter(ProceedingJoinPoint joinPoint) throws Throwable {
        Session session = entityManager.unwrap(Session.class);
        UUID restaurantUuid = RestaurantContext.getCurrentRestaurant();

        if (restaurantUuid != null) {
            Filter filter = session.enableFilter("restaurantFilter");
            filter.setParameter("restaurantUuid", restaurantUuid);
        }

        try {
            return joinPoint.proceed();
        } finally {
            if (restaurantUuid != null) {
                session.disableFilter("restaurantFilter");
            }
        }
    }
}