package com.kaiho.gastromanager.infrastructure.menu.output.jpa.entity;

import com.kaiho.gastromanager.infrastructure.common.model.Auditable;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import com.kaiho.gastromanager.infrastructure.submenu.output.jpa.entity.SubmenuEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "menus")
@Filter(name = "restaurantFilter", condition = "restaurant_uuid = :restaurantUuid")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class MenuEntity extends Auditable implements Serializable {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    private boolean isEnabled;
    @ManyToOne
    @JoinColumn(name = "restaurant_uuid", referencedColumnName = "uuid", nullable = false)
    private RestaurantEntity restaurant;

    @OneToMany(mappedBy = "menu")
    private Set<SubmenuEntity> submenus = new HashSet<>();

    public void addSubmenu(SubmenuEntity submenu) {
        submenus.add(submenu);
        submenu.setMenu(this);
    }

    public void removeSubmenu(SubmenuEntity submenu) {
        if (submenus != null) {
            submenus.remove(submenu);
            submenu.setMenu(null);
        }
    }

}
