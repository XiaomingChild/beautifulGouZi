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
    private Integer id;

    @Column(nullable = false, unique = true)
    private String account; // 账号
    
    @Column(nullable = false)
    private String password; // 密码
    
    private String nickname;
    private String phone;
    private String avatar;
    private String bio; // 个性签名

    @Convert(converter = JsonListConverter.class)
    @Column(columnDefinition = "JSON")
    private List<Integer> selected = new ArrayList<>(); // 收藏的电影ID列表 (JSON)
}
