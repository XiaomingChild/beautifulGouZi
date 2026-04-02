package com.heavenfilms.backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    private String id; // 订单号，建议用时间戳生成

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "schedule_id")
    private Integer scheduleId;

    private String seats; // 已选座位，逗号分隔，如 "3排5座,3排6座"
    
    @Column(name = "total_price")
    private Double totalPrice;

    private Integer status; // 1:已支付, 2:待支付, 3:已取消

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "created_at")
    private Date createdAt;
}
