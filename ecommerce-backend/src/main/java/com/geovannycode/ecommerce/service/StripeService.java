package com.geovannycode.ecommerce.service;

import com.geovannycode.ecommerce.dto.request.CheckoutItem;
import com.geovannycode.ecommerce.dto.request.CheckoutRequest;
import com.geovannycode.ecommerce.dto.response.CheckoutResponse;
import com.geovannycode.ecommerce.model.Product;
import com.geovannycode.ecommerce.repository.ProductRepository;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class StripeService {
    private static final Logger logger = LoggerFactory.getLogger(StripeService.class);

    private final ProductRepository productRepository;
    private final OrderService orderService;

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    public StripeService(ProductRepository productRepository, OrderService orderService) {
        this.productRepository = productRepository;
        this.orderService = orderService;
    }

    public CheckoutResponse createCheckoutSession(CheckoutRequest request) {
        try {
            Stripe.apiKey = stripeSecretKey;

            // Validar y obtener productos
            List<Product> products = validateAndGetProducts(request.getItems());

            // Crear line items para Stripe
            List<SessionCreateParams.LineItem> lineItems = createLineItems(request.getItems(), products);

            // Configurar sesión de checkout
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(request.getSuccessUrl())
                    .setCancelUrl(request.getCancelUrl())
                    .setCustomerEmail(request.getCustomerEmail())
                    .addAllLineItem(lineItems)
                    .putMetadata("integration_check", "accept_a_payment")
                    .putMetadata("customer_email", request.getCustomerEmail())
                    .build();

            Session session = Session.create(params);

            logger.info("✅ Stripe checkout session created: {}", session.getId());

            return new CheckoutResponse(
                    session.getId(),
                    session.getUrl(),
                    "Checkout session created successfully"
            );

        } catch (Exception e) {
            logger.error("❌ Error creating Stripe checkout session", e);
            throw new RuntimeException("Error al crear sesión de pago: " + e.getMessage());
        }
    }

    private List<Product> validateAndGetProducts(List<CheckoutItem> items) {
        List<String> productIds = items.stream()
                .map(CheckoutItem::getProductId)
                .toList();

        List<Product> products = productRepository.findAllById(productIds);

        if (products.size() != productIds.size()) {
            throw new RuntimeException("Algunos productos no existen");
        }

        return products;
    }

    private List<SessionCreateParams.LineItem> createLineItems(
            List<CheckoutItem> items, List<Product> products) {

        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();

        for (CheckoutItem item : items) {
            Product product = products.stream()
                    .filter(p -> p.getId().equals(item.getProductId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + item.getProductId()));

            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                    .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("usd")
                                    .setProductData(
                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                    .setName(product.getName())
                                                    .setDescription(product.getDescription())
                                                    .putMetadata("product_id", product.getId())
                                                    .build()
                                    )
                                    .setUnitAmount(product.getPrice().multiply(BigDecimal.valueOf(100)).longValue())
                                    .build()
                    )
                    .setQuantity(item.getQuantity().longValue())
                    .build();

            lineItems.add(lineItem);
        }

        return lineItems;
    }
}
