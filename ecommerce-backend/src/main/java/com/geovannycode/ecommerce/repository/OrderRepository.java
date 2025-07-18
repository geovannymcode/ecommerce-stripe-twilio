package com.geovannycode.ecommerce.repository;

import com.geovannycode.ecommerce.model.Order;
import com.geovannycode.ecommerce.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByStripeSessionId(String stripeSessionId);
    List<Order> findByCustomerEmailOrderByCreatedAtDesc(String customerEmail);
    List<Order> findByStatusOrderByCreatedAtDesc(OrderStatus status);
}
