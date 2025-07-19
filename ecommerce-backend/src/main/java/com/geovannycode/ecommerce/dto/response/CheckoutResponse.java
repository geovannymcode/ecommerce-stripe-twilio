package com.geovannycode.ecommerce.dto.response;

public class CheckoutResponse {
    private String sessionId;
    private String url;
    private String message;

    public CheckoutResponse() {}

    public CheckoutResponse(String sessionId, String url, String message) {
        this.sessionId = sessionId;
        this.url = url;
        this.message = message;
    }

    // Getters and Setters
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}

