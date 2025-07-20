package com.geovannycode.ecommerce.controller;

import com.geovannycode.ecommerce.dto.request.CheckoutRequest;
import com.geovannycode.ecommerce.dto.response.ApiResponse;
import com.geovannycode.ecommerce.dto.response.CheckoutResponse;
import com.geovannycode.ecommerce.service.StripeService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checkout")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class CheckoutController {
    private static final Logger logger = LoggerFactory.getLogger(CheckoutController.class);

    private final StripeService stripeService;

    public CheckoutController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/create-session")
    public ResponseEntity<ApiResponse<CheckoutResponse>> createCheckoutSession(
            @Valid @RequestBody CheckoutRequest request) {
        try {
            CheckoutResponse response = stripeService.createCheckoutSession(request);
            return ResponseEntity.ok(ApiResponse.success(response, "Sesión de checkout creada"));
        } catch (RuntimeException e) {
            logger.error("Error creating checkout session", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error creating checkout session", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Error interno del servidor"));
        }
    }
}
