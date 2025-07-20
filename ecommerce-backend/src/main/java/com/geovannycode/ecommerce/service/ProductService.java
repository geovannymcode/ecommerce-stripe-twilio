package com.geovannycode.ecommerce.service;

import com.geovannycode.ecommerce.model.Product;
import com.geovannycode.ecommerce.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findByOrderByCreatedAtDesc();
    }

    public Product getProductById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));
    }

    public List<Product> searchProducts(String name) {
        return productRepository.findByNameContaining(name);
    }

    @Transactional
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(String id, Product productUpdate) {
        Product product = getProductById(id);

        product.setName(productUpdate.getName());
        product.setDescription(productUpdate.getDescription());
        product.setPrice(productUpdate.getPrice());
        product.setEmoji(productUpdate.getEmoji());

        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(String id) {
        Product product = getProductById(id);
        productRepository.delete(product);
        logger.info("Product deleted: {}", id);
    }
}
