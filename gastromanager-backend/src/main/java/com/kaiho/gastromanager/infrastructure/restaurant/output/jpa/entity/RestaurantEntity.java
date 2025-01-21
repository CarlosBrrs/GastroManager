package com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity;

import com.kaiho.gastromanager.infrastructure.common.model.Auditable;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.entity.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "restaurants")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class RestaurantEntity extends Auditable implements Serializable {

    private String name;
    private String address;
    private String description;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.EAGER)
    private List<UserEntity> employees;

    @ManyToOne
    @JoinColumn(name = "owner_uuid", referencedColumnName = "uuid")
    private UserEntity owner;

    public void assignOwner(UserEntity owner) {
        this.owner = owner;
        owner.addRestaurantToOwner(this);
    }

    public void addEmployee(UserEntity employee) {
        this.employees.add(employee);
        employee.setRestaurant(this);
    }
}
