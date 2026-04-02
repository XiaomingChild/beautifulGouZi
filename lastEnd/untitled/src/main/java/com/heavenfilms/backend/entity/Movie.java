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
    private Integer id;

    private String title;

    @Column(name = "poster_url")
    private String posterUrl;

    private String director;

    @Column(columnDefinition = "TEXT")
    private String cast;

    private String genre;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "release_date")
    @Temporal(TemporalType.DATE)
    private Date releaseDate;

    @Column(columnDefinition = "TEXT")
    private String synopsis;

    private BigDecimal rating;
}
