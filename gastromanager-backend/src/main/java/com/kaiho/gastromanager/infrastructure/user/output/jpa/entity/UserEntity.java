package com.kaiho.gastromanager.infrastructure.user.output.jpa.entity;

import com.kaiho.gastromanager.infrastructure.common.model.Auditable;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.entity.OrderEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuperBuilder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_users")
@Getter
public class UserEntity extends Auditable implements Serializable {

    private String name;
    private String lastname;
    private String phone;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String encodedPassword;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "_users_roles",
            joinColumns = @JoinColumn(name = "user_uuid"),
            inverseJoinColumns = @JoinColumn(name = "role_uuid")
    )
    private Set<RoleEntity> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderEntity> orders;

    public void addOrder(OrderEntity order) {
        orders.add(order);
        order.setUser(this);
    }

}