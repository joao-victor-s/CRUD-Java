package com.crudjava.product.domain.service;

import com.crudjava.application.service.ProductService;
import com.crudjava.product.domain.model.Product;
import com.crudjava.application.dto.ProductRequest;
import com.crudjava.application.dto.ProductResponse;
import com.crudjava.infra.persistence.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private ProductRequest validRequest;
    private Product savedProduct;

    @BeforeEach
    void setUp() {
        validRequest = new ProductRequest("Mouse", 99.90);
        savedProduct = Product.createWithId(1L, "Mouse", 99.90);
    }

    @Test
    void saveProduct_shouldSaveAndReturnResponse() {
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        ProductResponse response = productService.saveProduct(validRequest);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Mouse", response.name());
        assertEquals(99.90, response.price(), 1e-9);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());
        assertEquals("Mouse", captor.getValue().getName());
        assertEquals(99.90, captor.getValue().getPrice(), 1e-9);
    }

    @Test
    void getProductById_shouldReturnProduct_whenExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(savedProduct));

        ProductResponse response = productService.getProductById(1L);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Mouse", response.name());
        assertEquals(99.90, response.price(), 1e-9);
        verify(productRepository).findById(1L);
    }

    @Test
    void getProductById_shouldThrow_whenNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.getProductById(99L));
        verify(productRepository).findById(99L);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void updateProduct_shouldUpdateAndReturnResponse_whenExists() {
        ProductRequest updateRequest = new ProductRequest("Mouse Gamer", 199.90);
        Product updatedSaved = Product.createWithId(1L, "Mouse Gamer", 199.90);

        when(productRepository.findById(1L)).thenReturn(Optional.of(savedProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedSaved);

        ProductResponse response = productService.updateProduct(updateRequest, 1L);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Mouse Gamer", response.name());
        assertEquals(199.90, response.price(), 1e-9);

        InOrder inOrder = inOrder(productRepository);
        inOrder.verify(productRepository).findById(1L);
        inOrder.verify(productRepository).save(any(Product.class));
    }

    @Test
    void updateProduct_shouldThrow_whenNotFound() {
        ProductRequest updateRequest = new ProductRequest("Mouse Gamer", 199.90);
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.updateProduct(updateRequest, 99L));
        verify(productRepository).findById(99L);
        verify(productRepository, never()).save(any());
    }

    @Test
    void deleteProductById_shouldDelete_whenExists() {
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProductById(1L);

        verify(productRepository).existsById(1L);
        verify(productRepository).deleteById(1L);
    }

    @Test
    void deleteProductById_shouldThrow_whenNotFound() {
        when(productRepository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> productService.deleteProductById(99L));
        verify(productRepository).existsById(99L);
        verify(productRepository, never()).deleteById(anyLong());
    }
}
