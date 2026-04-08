package com.heavenfilms.backend.service.impl;

import com.heavenfilms.backend.entity.BookedSeat;
import com.heavenfilms.backend.entity.Order;
import com.heavenfilms.backend.exception.ServiceException;
import com.heavenfilms.backend.repository.BookedSeatRepository;
import com.heavenfilms.backend.repository.OrderRepository;
import com.heavenfilms.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private BookedSeatRepository bookedSeatRepository;

    @Override
    @Transactional
    public Order createOrder(Order order) {
        String orderNo = "HF" + System.currentTimeMillis() + (int)((Math.random() * 9 + 1) * 1000);
        order.setOrderNo(orderNo);
        order.setStatus(0);
        
        String[] seats = order.getSeatInfo().split(",");
        for (String seat : seats) {
            String[] pos = seat.replace("排", "-").replace("座", "").split("-");
            if (pos.length != 2) continue;
            int row = Integer.parseInt(pos[0]);
            int col = Integer.parseInt(pos[1]);
            
            if (bookedSeatRepository.existsByScheduleIdAndRowIndexAndColIndex(order.getScheduleId(), row, col)) {
                throw new ServiceException("座位 " + seat + " 已被占用，请重新选择");
            }
        }

        Order savedOrder = orderRepository.save(order);

        for (String seat : seats) {
            String[] pos = seat.replace("排", "-").replace("座", "").split("-");
            if (pos.length != 2) continue;
            BookedSeat bookedSeat = new BookedSeat();
            bookedSeat.setScheduleId(order.getScheduleId());
            bookedSeat.setOrderId(savedOrder.getId());
            bookedSeat.setRowIndex(Integer.parseInt(pos[0]));
            bookedSeat.setColIndex(Integer.parseInt(pos[1]));
            bookedSeat.setStatus(2);
            bookedSeatRepository.save(bookedSeat);
        }

        return savedOrder;
    }

    @Override
    @Transactional
    public void payOrder(String orderNo) {
        Order order = orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new ServiceException("订单不存在"));
        
        if (order.getStatus() != 0) {
            throw new ServiceException("订单状态异常");
        }

        order.setStatus(1);
        order.setPayTime(new Date());
        order.setTicketCode(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        orderRepository.save(order);

        List<BookedSeat> seats = bookedSeatRepository.findByScheduleId(order.getScheduleId());
        for (BookedSeat seat : seats) {
            if (order.getId().equals(seat.getOrderId())) {
                seat.setStatus(1);
                bookedSeatRepository.save(seat);
            }
        }
    }

    @Override
    public List<BookedSeat> getBookedSeats(Long scheduleId) {
        return bookedSeatRepository.findByScheduleId(scheduleId);
    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserIdOrderByCreateTimeDesc(userId);
    }

    @Override
    public Order getOrderByNo(String orderNo) {
        return orderRepository.findByOrderNo(orderNo).orElse(null);
    }
}
