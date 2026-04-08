package com.heavenfilms.backend.service;

import com.heavenfilms.backend.entity.BookedSeat;
import com.heavenfilms.backend.entity.Order;
import java.util.List;

public interface OrderService {
    Order createOrder(Order order);
    void payOrder(String orderNo);
    List<BookedSeat> getBookedSeats(Long scheduleId);
    List<Order> getUserOrders(Long userId);
    Order getOrderByNo(String orderNo);
}
