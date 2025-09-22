package com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity;

import com.kaiho.gastromanager.infrastructure.common.model.Auditable;
import com.kaiho.gastromanager.infrastructure.invoice.output.jpa.entity.InvoiceEntity;
import com.kaiho.gastromanager.infrastructure.menu.output.jpa.entity.MenuEntity;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.entity.OrderEntity;
import com.kaiho.gastromanager.infrastructure.payment.output.jpa.entity.PaymentEntity;
import com.kaiho.gastromanager.infrastructure.product.output.jpa.entity.ProductEntity;
import com.kaiho.gastromanager.infrastructure.restaurantconfig.output.jpa.entity.RestaurantConfigEntity;
import com.kaiho.gastromanager.infrastructure.submenu.output.jpa.entity.SubmenuEntity;
import com.kaiho.gastromanager.infrastructure.taxconfig.output.jpa.entity.TaxConfigEntity;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.entity.UserEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.EAGER)
    private List<MenuEntity> menus;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.EAGER)
    private List<SubmenuEntity> submenus;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_config_uuid", referencedColumnName = "uuid")
    private RestaurantConfigEntity restaurantConfig;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.EAGER)
    private List<OrderEntity> orders;
/*
    @OneToMany(mappedBy = "restaurant", fetch = FetchType.EAGER)
    private List<InvoiceEntity> invoices;*/

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentEntity> payments = new ArrayList<>();

    public void addPayment(PaymentEntity payment) {
        payments.add(payment);
        payment.setRestaurant(this);
    }

    public void removePayment(PaymentEntity payment) {
        payments.remove(payment);
        payment.setRestaurant(null);
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "restaurant_taxes",
            joinColumns = @JoinColumn(name = "restaurant_uuid"),
            inverseJoinColumns = @JoinColumn(name = "tax_configuration_uuid")
    )
    @Builder.Default
    private List<TaxConfigEntity> taxConfigs = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "owner_uuid", referencedColumnName = "uuid")
    private UserEntity owner;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductEntity> products = new ArrayList<>();

    public void addProduct(ProductEntity product) {
        products.add(product);
        product.setRestaurant(this);
    }

    public void removeProduct(ProductEntity product) {
        products.remove(product);
        product.setRestaurant(null);
    }

    public void addTaxConfiguration(TaxConfigEntity taxConfig) {
        this.taxConfigs.add(taxConfig);
        taxConfig.getRestaurants().add(this);
    }

    public void assignOwner(UserEntity owner) {
        this.owner = owner;
        owner.addRestaurantToOwner(this);
    }

    public void addEmployee(UserEntity employee) {
        this.employees.add(employee);
        employee.setRestaurant(this);
    }

    public void addMenu(MenuEntity menu) {
        this.menus.add(menu);
        menu.setRestaurant(this);
    }

    public void addOrder(OrderEntity orderEntity) {
        this.orders.add(orderEntity);
        orderEntity.setRestaurant(this);
    }

    public void setConfigs(RestaurantConfigEntity configs) {
        this.restaurantConfig = configs;
        configs.setRestaurant(this);
    }
/*
    public void addInvoice(InvoiceEntity invoiceEntity) {
        this.invoices.add(invoiceEntity);
        invoiceEntity.setRestaurant(this);
    }*/

    public void addSubmenu(SubmenuEntity submenu) {
        if (submenu == null) {
            return;
        }
        if (this.menus == null) {
            this.menus = new ArrayList<>();
        }
        submenu.setRestaurant(this);
    }
}
