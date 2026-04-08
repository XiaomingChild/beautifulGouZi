package com.heavenfilms.backend.repository;

import com.heavenfilms.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByScheduleIdAndStatusNot(Long scheduleId, Integer status);
    List<Order> findByUserIdOrderByCreateTimeDesc(Long userId);
    Optional<Order> findByOrderNo(String orderNo);
}
