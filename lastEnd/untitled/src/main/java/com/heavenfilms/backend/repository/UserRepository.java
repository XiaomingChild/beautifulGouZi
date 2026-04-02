package com.heavenfilms.backend.repository;

import com.heavenfilms.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    // 根据账号查找用户
    User findByAccount(String account);
}
