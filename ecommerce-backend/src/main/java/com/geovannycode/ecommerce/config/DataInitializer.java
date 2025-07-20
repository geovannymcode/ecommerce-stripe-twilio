package com.geovannycode.ecommerce.config;

import com.geovannycode.ecommerce.model.Product;
import com.geovannycode.ecommerce.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DataInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        // Inicializar productos si no existen
        if (productRepository.count() == 0) {
            productRepository.save(new Product(
                    "laptop_001",
                    "MacBook Pro 16\"",
                    "Laptop profesional con chip M3 Pro, 18GB RAM, 512GB SSD",
                    BigDecimal.valueOf(2499.00),
                    "💻"
            ));

            productRepository.save(new Product(
                    "phone_001",
                    "iPhone 15 Pro",
                    "Smartphone premium con cámara profesional, chip A17 Pro",
                    BigDecimal.valueOf(1199.00),
                    "📱"
            ));

            productRepository.save(new Product(
                    "headphones_001",
                    "AirPods Pro 2",
                    "Auriculares inalámbricos con cancelación activa de ruido",
                    BigDecimal.valueOf(249.00),
                    "🎧"
            ));

            System.out.println("✅ Productos de ejemplo creados");
        }
    }
}
