package com.heavenfilms.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@Entity
@Table(name = "schedules")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "movie_id", nullable = false)
    private Long movieId;

    @Column(name = "cinema_id", nullable = false)
    private Long cinemaId;

    @Column(name = "hall_id", nullable = false)
    private Long hallId;

    @Column(name = "show_date", nullable = false)
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date showDate;

    @Column(name = "start_time", nullable = false)
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    private Time startTime;

    @Column(name = "end_time", nullable = false)
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    private Time endTime;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "language_version")
    private String languageVersion;

    // 关联关系映射
    @ManyToOne
    @JoinColumn(name = "movie_id", insertable = false, updatable = false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "cinema_id", insertable = false, updatable = false)
    private Cinema cinema;

    @ManyToOne
    @JoinColumn(name = "hall_id", insertable = false, updatable = false)
    private Hall hall;
}
