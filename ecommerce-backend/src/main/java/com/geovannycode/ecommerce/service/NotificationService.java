package com.geovannycode.ecommerce.service;

import com.geovannycode.ecommerce.model.Notification;
import com.geovannycode.ecommerce.model.Order;
import com.geovannycode.ecommerce.model.enums.NotificationType;
import com.geovannycode.ecommerce.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final EmailService emailService;
    private final TwilioService twilioService;
    private final NotificationRepository notificationRepository;

    public NotificationService(EmailService emailService,
                               TwilioService twilioService,
                               NotificationRepository notificationRepository) {
        this.emailService = emailService;
        this.twilioService = twilioService;
        this.notificationRepository = notificationRepository;
    }

    @Async
    public void sendOrderConfirmation(Order order) {
        try {
            // Enviar email
            if (order.getCustomerEmail() != null) {
                String emailMessage = createEmailMessage(order);
                emailService.sendOrderConfirmation(order.getCustomerEmail(), emailMessage);

                saveNotification(order, NotificationType.EMAIL,
                        order.getCustomerEmail(), emailMessage, "SENT");
            }

            // Enviar SMS (usando número de prueba)
            String smsMessage = createSMSMessage(order);
            twilioService.sendSMS("+1234567890", smsMessage); // Número de prueba

            saveNotification(order, NotificationType.SMS,
                    "+1234567890", smsMessage, "SENT");

            logger.info("✅ Notifications sent for order: {}", order.getId());

        } catch (Exception e) {
            logger.error("❌ Error sending notifications for order: {}", order.getId(), e);
        }
    }

    private String createEmailMessage(Order order) {
        return String.format(
                "¡Gracias por tu compra!\n\n" +
                        "Detalles de tu orden:\n" +
                        "ID: %s\n" +
                        "Total: $%.2f %s\n" +
                        "Estado: %s\n\n" +
                        "Tu pedido será procesado pronto.\n\n" +
                        "Saludos,\nEquipo TechStore",
                order.getId(),
                order.getAmountTotal() / 100.0,
                order.getCurrency().toUpperCase(),
                order.getStatus()
        );
    }

    private String createSMSMessage(Order order) {
        return String.format(
                "TechStore: ¡Gracias por tu compra! Total: $%.2f. Tu pedido #%s será procesado pronto.",
                order.getAmountTotal() / 100.0,
                order.getId()
        );
    }

    private void saveNotification(Order order, NotificationType type,
                                  String recipient, String message, String status) {
        Notification notification = new Notification(order, type, recipient, message, status);
        notificationRepository.save(notification);
    }
}
