package com.geovannycode.ecommerce.service;

import com.geovannycode.ecommerce.model.Order;
import com.geovannycode.ecommerce.model.OrderItem;
import com.geovannycode.ecommerce.model.Product;
import com.geovannycode.ecommerce.model.enums.OrderStatus;
import com.geovannycode.ecommerce.model.enums.PaymentStatus;
import com.geovannycode.ecommerce.repository.OrderItemRepository;
import com.geovannycode.ecommerce.repository.OrderRepository;
import com.geovannycode.ecommerce.repository.ProductRepository;
import com.stripe.model.checkout.Session;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final NotificationService notificationService;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        OrderItemRepository orderItemRepository,
                        NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.notificationService = notificationService;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByEmail(String email) {
        return orderRepository.findByCustomerEmailOrderByCreatedAtDesc(email);
    }

    public Order createOrderFromStripeSession(Session stripeSession) {
        try {
            // Verificar si ya existe una orden para esta sesión
            if (orderRepository.findByStripeSessionId(stripeSession.getId()).isPresent()) {
                logger.warn("Order already exists for session: {}", stripeSession.getId());
                return orderRepository.findByStripeSessionId(stripeSession.getId()).get();
            }

            // Crear nueva orden
            Order order = new Order();
            order.setStripeSessionId(stripeSession.getId());
            order.setCustomerEmail(stripeSession.getCustomerEmail());
            order.setAmountTotal(stripeSession.getAmountTotal());
            order.setCurrency(stripeSession.getCurrency());
            order.setStatus(OrderStatus.PROCESSING);
            order.setPaymentStatus(PaymentStatus.PAID);

            order = orderRepository.save(order);

            // Crear items de la orden (esto requeriría metadata adicional de Stripe)
            // Por ahora, creamos un ejemplo básico
            createOrderItemsFromSession(order, stripeSession);

            logger.info("✅ Order created successfully: {}", order.getId());

            // Enviar notificaciones asíncronamente
            notificationService.sendOrderConfirmation(order);

            return order;

        } catch (Exception e) {
            logger.error("❌ Error creating order from Stripe session", e);
            throw new RuntimeException("Error al crear orden: " + e.getMessage());
        }
    }

    private void createOrderItemsFromSession(Order order, Session stripeSession) {
        // En un caso real, obtendrías los items desde los metadata de Stripe
        // Por simplicidad, creamos items de ejemplo
        List<Product> products = productRepository.findAll();

        if (!products.isEmpty()) {
            Product product = products.get(0); // Tomar el primer producto como ejemplo
            OrderItem item = new OrderItem(order, product, 1, product.getPrice());
            orderItemRepository.save(item);
        }
    }

    public void updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        order.setStatus(status);
        orderRepository.save(order);

        logger.info("Order status updated: {} -> {}", orderId, status);
    }
}
