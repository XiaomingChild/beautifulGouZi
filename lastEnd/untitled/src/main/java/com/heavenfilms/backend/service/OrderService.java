package com.heavenfilms.backend.service;

import com.heavenfilms.backend.entity.Order;

import java.util.List;

public interface OrderService {
    Order createOrder(Order order);
    List<String> getSoldSeats(Integer scheduleId);
    List<Order> getUserOrders(Integer userId);
}
