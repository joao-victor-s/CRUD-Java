package com.crudjava.service;

import com.crudjava.dto.ProductRequest;
import com.crudjava.dto.ProductResponse;
import com.crudjava.entity.Product;
import com.crudjava.repository.ProductRepository;
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
        Product product = new Product();
        product.setName(dto.name());
        product.setPrice(dto.price());

        Product p = productRepository.save(product);
        return toResponse(p);
    }

    @Transactional
    public ProductResponse updateProduct(ProductRequest product, Long id) {
        Product oldProduct = findProductById(id);

        oldProduct.setName(product.name());
        oldProduct.setPrice(product.price());
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
