package com.heavenfilms.backend.service.impl;

import com.heavenfilms.backend.entity.Order;
import com.heavenfilms.backend.repository.OrderRepository;
import com.heavenfilms.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order createOrder(Order order) {
        order.setId("ORD" + System.currentTimeMillis());
        order.setCreatedAt(new Date());
        order.setStatus(1); // 默认支付成功
        return orderRepository.save(order);
    }

    @Override
    public List<String> getSoldSeats(Integer scheduleId) {
        List<Order> orders = orderRepository.findByScheduleIdAndStatusNot(scheduleId, 3);
        return orders.stream()
                .flatMap(o -> Arrays.stream(o.getSeats().split(",")))
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> getUserOrders(Integer userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}
