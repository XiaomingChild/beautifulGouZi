package com.heavenfilms.backend.controller;

import com.heavenfilms.backend.common.Result;
import com.heavenfilms.backend.entity.BookedSeat;
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

    @PostMapping("/pay/{orderNo}")
    public Result<String> payOrder(@PathVariable String orderNo) {
        orderService.payOrder(orderNo);
        return Result.success("支付成功");
    }

    @GetMapping("/booked-seats/{scheduleId}")
    public Result<List<BookedSeat>> getBookedSeats(@PathVariable Long scheduleId) {
        return Result.success(orderService.getBookedSeats(scheduleId));
    }

    @GetMapping("/user/{userId}")
    public Result<List<Order>> getUserOrders(@PathVariable Long userId) {
        return Result.success(orderService.getUserOrders(userId));
    }

    @GetMapping("/detail/{orderNo}")
    public Result<Order> getOrderDetail(@PathVariable String orderNo) {
        return Result.success(orderService.getOrderByNo(orderNo));
    }
}
