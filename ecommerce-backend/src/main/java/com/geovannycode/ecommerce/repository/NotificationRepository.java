package com.geovannycode.ecommerce.repository;

import com.geovannycode.ecommerce.model.Notification;
import com.geovannycode.ecommerce.model.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByOrderId(Long orderId);
    List<Notification> findByTypeAndStatus(NotificationType type, String status);
}
