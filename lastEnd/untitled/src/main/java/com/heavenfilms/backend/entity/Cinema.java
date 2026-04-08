package com.heavenfilms.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cinemas")
public class Cinema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    private String city;

    private String district;

    private String phone;

    @Column(precision = 3, scale = 1)
    private java.math.BigDecimal rating;

    @Column(name = "img_url")
    private String imgUrl;

    private String services;
}
