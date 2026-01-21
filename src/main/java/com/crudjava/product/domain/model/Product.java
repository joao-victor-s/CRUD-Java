package com.crudjava.product.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

    protected Product() { } // JPA

    private Product(String name, Double price) {
        setName(name);
        setPrice(price);
    }

    private Product(Long id, String name, Double price) {
        this.id = id;
        setName(name);
        setPrice(price);
    }

    public static Product create(String name, Double price) {
        return new Product(name, price);
    }

    public static Product createWithId(Long id, String name, Double price) {
        return new Product(id, name, price);
    }

    public void update(String name, Double price) {
        setName(name);
        setPrice(price);
    }

    private void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name must not be blank.");
        }
        this.name = name.trim();
    }

    private void setPrice(Double price) {
        if (price == null) {
            throw new IllegalArgumentException("Product price must not be null.");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Product price must be greater than zero.");
        }
        this.price = price;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public Double getPrice() { return price; }
}
