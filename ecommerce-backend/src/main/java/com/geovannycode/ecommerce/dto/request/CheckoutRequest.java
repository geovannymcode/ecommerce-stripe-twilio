package com.geovannycode.ecommerce.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class CheckoutRequest {
    @NotEmpty(message = "Los items son requeridos")
    private List<CheckoutItem> items;

    @Email(message = "Email inválido")
    private String customerEmail;

    @NotBlank(message = "URL de éxito requerida")
    private String successUrl;

    @NotBlank(message = "URL de cancelación requerida")
    private String cancelUrl;

    // Constructors
    public CheckoutRequest() {}

    // Getters and Setters
    public List<CheckoutItem> getItems() { return items; }
    public void setItems(List<CheckoutItem> items) { this.items = items; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getSuccessUrl() { return successUrl; }
    public void setSuccessUrl(String successUrl) { this.successUrl = successUrl; }

    public String getCancelUrl() { return cancelUrl; }
    public void setCancelUrl(String cancelUrl) { this.cancelUrl = cancelUrl; }
}
