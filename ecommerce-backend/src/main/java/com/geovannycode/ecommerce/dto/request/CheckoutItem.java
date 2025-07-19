package com.geovannycode.ecommerce.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class CheckoutItem {
    @NotBlank(message = "ID del producto requerido")
    private String productId;

    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer quantity = 1;

    // Constructors
    public CheckoutItem() {}

    public CheckoutItem(String productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
