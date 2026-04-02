package com.heavenfilms.backend.repository;

import com.heavenfilms.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {
    // 获取指定场次的所有订单，用于统计已售座位
    List<Order> findByScheduleIdAndStatusNot(Integer scheduleId, Integer status);
    
    // 获取用户的订单列表
    List<Order> findByUserIdOrderByCreatedAtDesc(Integer userId);
}
