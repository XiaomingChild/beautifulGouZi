package com.heavenfilms.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@Entity
@Table(name = "schedules")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "movie_id")
    private Integer movieId;

    @Column(name = "cinema_id")
    private Integer cinemaId;

    @Column(name = "hall_name")
    private String hallName;

    @Column(name = "start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    private String type; // 如 "英语 3D", "国语 2D"
    private Double price;
    
    // 选填：为了方便前端，我们可以建立关联关系，或者在查询时手动处理
    @ManyToOne
    @JoinColumn(name = "cinema_id", insertable = false, updatable = false)
    private Cinema cinema;
}
