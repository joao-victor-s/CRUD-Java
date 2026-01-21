package com.crudjava.application.service;

import com.crudjava.application.dto.ProductRequest;
import com.crudjava.application.dto.ProductResponse;
import com.crudjava.product.domain.model.Product;
import com.crudjava.infra.persistence.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found."));
    }

    private ProductResponse toResponse(Product p) {
        return new ProductResponse(p.getId(), p.getName(), p.getPrice());
    }

    public ProductResponse getProductById(Long id) {
        Product p = findProductById(id);
        return toResponse(p);
    }

    @Transactional
    public ProductResponse saveProduct(ProductRequest dto) {
        Product product = Product.create(dto.name(), dto.price());
        Product p = productRepository.save(product);
        return toResponse(p);
    }

    @Transactional
    public ProductResponse updateProduct(ProductRequest product, Long id) {
        Product oldProduct = findProductById(id);
        oldProduct.update(product.name(), product.price());
        Product p = productRepository.save(oldProduct);

        return toResponse(p);
    }

    @Transactional
    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Product not found.");
        }
        productRepository.deleteById(id);
    }
}
