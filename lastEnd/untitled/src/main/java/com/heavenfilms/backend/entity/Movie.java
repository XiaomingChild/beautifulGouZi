package com.heavenfilms.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "english_title")
    private String englishTitle;

    @Column(name = "poster_url", nullable = false)
    private String posterUrl;

    private String director;
    private String actors;
    private String genre;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "release_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date releaseDate;

    @Column(columnDefinition = "TEXT")
    private String synopsis;

    @Column(precision = 3, scale = 1)
    private BigDecimal rating;

    @Column(name = "is_showing")
    private Integer isShowing; // 0-即将上映, 1-热映中
}
