package com.heavenfilms.backend.controller;

import com.heavenfilms.backend.common.Result;
import com.heavenfilms.backend.entity.Order;
import com.heavenfilms.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public Result<Order> createOrder(@RequestBody Order order) {
        Order savedOrder = orderService.createOrder(order);
        return Result.success(savedOrder);
    }

    @GetMapping("/sold-seats/{scheduleId}")
    public Result<List<String>> getSoldSeats(@PathVariable Integer scheduleId) {
        List<String> soldSeats = orderService.getSoldSeats(scheduleId);
        return Result.success(soldSeats);
    }

    @GetMapping("/user/{userId}")
    public Result<List<Order>> getUserOrders(@PathVariable Integer userId) {
        List<Order> orders = orderService.getUserOrders(userId);
        return Result.success(orders);
    }
}
