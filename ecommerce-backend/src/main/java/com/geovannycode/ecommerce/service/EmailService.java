package com.geovannycode.ecommerce.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOrderConfirmation(String toEmail, String message) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(fromEmail);
            mailMessage.setTo(toEmail);
            mailMessage.setSubject("Confirmación de Pedido - TechStore");
            mailMessage.setText(message);

            mailSender.send(mailMessage);
            logger.info("✅ Email sent to: {}", toEmail);

        } catch (Exception e) {
            logger.error("❌ Error sending email to: {}", toEmail, e);
            throw new RuntimeException("Error al enviar email: " + e.getMessage());
        }
    }
}
