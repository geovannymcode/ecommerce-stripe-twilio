package com.geovannycode.ecommerce.controller;

import com.geovannycode.ecommerce.model.Order;
import com.geovannycode.ecommerce.service.OrderService;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook")
public class WebhookController {
    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    private final OrderService orderService;

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    public WebhookController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        try {
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

            logger.info("🔔 Stripe webhook received: {}", event.getType());

            switch (event.getType()) {
                case "checkout.session.completed":
                    handleCheckoutSessionCompleted(event);
                    break;
                case "payment_intent.succeeded":
                    handlePaymentSucceeded(event);
                    break;
                case "payment_intent.payment_failed":
                    handlePaymentFailed(event);
                    break;
                default:
                    logger.info("Unhandled event type: {}", event.getType());
            }

            return ResponseEntity.ok("Webhook processed");

        } catch (Exception e) {
            logger.error("❌ Error processing Stripe webhook", e);
            return ResponseEntity.badRequest().body("Webhook error");
        }
    }

    private void handleCheckoutSessionCompleted(Event event) {
        try {
            Session session = (Session) event.getDataObjectDeserializer()
                    .getObject().orElse(null);

            if (session != null) {
                logger.info("💰 Processing completed checkout session: {}", session.getId());
                Order order = orderService.createOrderFromStripeSession(session);
                logger.info("✅ Order created from webhook: {}", order.getId());
            }
        } catch (Exception e) {
            logger.error("Error handling checkout.session.completed", e);
        }
    }

    private void handlePaymentSucceeded(Event event) {
        logger.info("✅ Payment succeeded webhook processed");
    }

    private void handlePaymentFailed(Event event) {
        logger.error("❌ Payment failed webhook processed");
    }
}
