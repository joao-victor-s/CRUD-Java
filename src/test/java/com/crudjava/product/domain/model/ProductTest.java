package com.crudjava.product.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void create_shouldThrow_whenNameNullOrBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> Product.create(null, 10.0));

        assertThrows(IllegalArgumentException.class,
                () -> Product.create("   ", 10.0));
    }

    @Test
    void create_shouldThrow_whenPriceNull() {
        assertThrows(IllegalArgumentException.class,
                () -> Product.create("Mouse", null));
    }

    @Test
    void create_shouldThrow_whenPriceZeroOrNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> Product.create("Mouse", 0.0));

        assertThrows(IllegalArgumentException.class,
                () -> Product.create("Mouse", -1.0));
    }

    @Test
    void update_shouldApplySameRulesAsCreate() {
        Product p = Product.create("Mouse", 10.0);

        assertThrows(IllegalArgumentException.class,
                () -> p.update("", 10.0));

        assertThrows(IllegalArgumentException.class,
                () -> p.update("Mouse", 0.0));
    }
}
