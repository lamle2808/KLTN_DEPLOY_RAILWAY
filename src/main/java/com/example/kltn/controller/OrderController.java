package com.example.kltn.controller;

import com.example.kltn.entity.Order;
import com.example.kltn.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        try {
            List<Order> orders = orderService.getAll();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        try {
            Order order = orderService.getById(id);
            if (order == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        try {
            if (order == null) {
                return ResponseEntity.badRequest().build();
            }
            Order createdOrder = orderService.saveOrUpdate(order);
            return ResponseEntity.ok(createdOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order order) {
        try {
            Order existingOrder = orderService.getById(id);
            if (existingOrder == null) {
                return ResponseEntity.notFound().build();
            }
            order.setId(id);
            Order updatedOrder = orderService.saveOrUpdate(order);
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        try {
            Order order = orderService.getById(id);
            if (order == null) {
                return ResponseEntity.notFound().build();
            }
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Order>> getOrdersByEmployee(@PathVariable Long employeeId) {
        try {
            if (employeeId == null) {
                return ResponseEntity.badRequest().build();
            }
            List<Order> orders = orderService.getAll().stream()
                    .filter(order -> order.getAssignedEmployee() != null && 
                            order.getAssignedEmployee().getId().equals(employeeId))
                    .toList();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam(required = true) String status) {
        try {
            if (status == null || status.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            Order order = orderService.getById(orderId);
            if (order == null) {
                return ResponseEntity.notFound().build();
            }
            order.setStatus(status.trim());
            Order updatedOrder = orderService.saveOrUpdate(order);
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
} 