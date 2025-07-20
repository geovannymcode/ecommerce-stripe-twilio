package com.geovannycode.ecommerce.controller;

import com.geovannycode.ecommerce.dto.request.OrderStatusRequest;
import com.geovannycode.ecommerce.dto.response.ApiResponse;
import com.geovannycode.ecommerce.model.Order;
import com.geovannycode.ecommerce.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Order>>> getAllOrders(
            @RequestParam(required = false) String email) {
        try {
            List<Order> orders = email != null ?
                    orderService.getOrdersByEmail(email) :
                    orderService.getAllOrders();
            return ResponseEntity.ok(ApiResponse.success(orders));
        } catch (Exception e) {
            logger.error("Error getting orders", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Error al obtener órdenes"));
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Void>> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody OrderStatusRequest request) {
        try {
            orderService.updateOrderStatus(id, request.getStatus());
            return ResponseEntity.ok(ApiResponse.success(null, "Estado actualizado"));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error updating order status", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Error al actualizar estado"));
        }
    }
}
