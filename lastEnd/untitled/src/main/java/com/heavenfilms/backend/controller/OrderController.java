package com.heavenfilms.backend.controller;

import com.heavenfilms.backend.entity.Order;
import com.heavenfilms.backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    /**
     * 下单接口
     */
    @PostMapping("/create")
    public Object createOrder(@RequestBody Order order) {
        // 生成简单订单号
        order.setId("ORD" + System.currentTimeMillis());
        order.setCreatedAt(new Date());
        order.setStatus(1); // 默认直接支付成功
        return orderRepository.save(order);
    }

    /**
     * 获取指定场次已售出的座位
     */
    @GetMapping("/sold-seats/{scheduleId}")
    public List<String> getSoldSeats(@PathVariable Integer scheduleId) {
        // 查找该场次所有有效订单（非取消状态）
        List<Order> orders = orderRepository.findByScheduleIdAndStatusNot(scheduleId, 3);
        // 将所有订单中的座位字符串合并并拆分为列表
        return orders.stream()
                .flatMap(o -> java.util.Arrays.stream(o.getSeats().split(",")))
                .collect(Collectors.toList());
    }

    /**
     * 获取个人订单
     */
    @GetMapping("/user/{userId}")
    public List<Order> getUserOrders(@PathVariable Integer userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}
