package com.heavenfilms.backend.entity;

import com.heavenfilms.backend.utils.JsonListConverter;
import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String account;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String nickname;

    @Column(unique = true)
    private String phone;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "created_at", insertable = false, updatable = false)
    private java.util.Date createdAt;
}
