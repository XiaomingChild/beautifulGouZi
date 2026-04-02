package com.heavenfilms.backend.controller;

import com.heavenfilms.backend.entity.User;
import com.heavenfilms.backend.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Data
    public static class RegisterRequest {
        private String account;
        private String password;
        private String nickname;
        private String phone;
    }

    @PostMapping("/login")
    public Object login(@RequestBody User loginUser) {
        User user = userRepository.findByAccount(loginUser.getAccount());
        if (user != null && user.getPassword().equals(loginUser.getPassword())) {
            return user;
        }
        return "账号或密码错误";
    }

    @PostMapping("/register")
    public Object register(@RequestBody RegisterRequest req) {
        // 1. 账号重复校验
        if (userRepository.findByAccount(req.getAccount()) != null) {
            return "账号已存在";
        }

        // 2. 创建并保存用户
        User newUser = new User();
        newUser.setAccount(req.getAccount());
        newUser.setPassword(req.getPassword());
        // 如果请求里没写昵称，就默认用账号名
        String nickname = (req.getNickname() != null && !req.getNickname().isEmpty()) 
                          ? req.getNickname() : req.getAccount();
        newUser.setNickname(nickname);
        newUser.setPhone(req.getPhone());
        return userRepository.save(newUser);
    }

    @PostMapping("/logout")
    public Object logout() {
        return "退出成功";
    }

    @PostMapping("/update")
    public Object update(@RequestBody User user) {
        if (user.getId() == null) {
            return "用户ID不能为空";
        }
        User existingUser = userRepository.findById(user.getId()).orElse(null);
        if (existingUser != null) {
            if (user.getNickname() != null) existingUser.setNickname(user.getNickname());
            if (user.getPhone() != null) existingUser.setPhone(user.getPhone());
            if (user.getAvatar() != null) existingUser.setAvatar(user.getAvatar());
            if (user.getBio() != null) existingUser.setBio(user.getBio());
            return userRepository.save(existingUser);
        }
        return "用户不存在";
    }
}
