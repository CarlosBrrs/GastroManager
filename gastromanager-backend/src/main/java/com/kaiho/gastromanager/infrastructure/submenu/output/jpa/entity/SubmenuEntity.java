package com.kaiho.gastromanager.infrastructure.submenu.output.jpa.entity;

import com.kaiho.gastromanager.infrastructure.common.model.Auditable;
import com.kaiho.gastromanager.infrastructure.menu.output.jpa.entity.MenuEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;

import java.io.Serializable;

@Entity
@Table(name = "submenus")
@Filter(name = "restaurantFilter", condition = "restaurant_uuid = :restaurantUuid")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class SubmenuEntity extends Auditable implements Serializable {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    private boolean isEnabled;
    @ManyToOne
    @JoinColumn(name = "restaurant_uuid", referencedColumnName = "uuid", nullable = false)
    private RestaurantEntity restaurant;

    @ManyToOne(optional = false)
    @JoinColumn(name = "menu_uuid")
    private MenuEntity menu;

}
