package com.heavenfilms.backend.controller;

import com.heavenfilms.backend.common.Result;
import com.heavenfilms.backend.dto.UserDTO;
import com.heavenfilms.backend.entity.User;
import com.heavenfilms.backend.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Data
    public static class RegisterRequest {
        private String account;
        private String password;
        private String nickname;
        private String phone;
    }

    @PostMapping("/login")
    public Result<UserDTO> login(@RequestBody User loginUser) {
        UserDTO userDTO = userService.login(loginUser.getAccount(), loginUser.getPassword());
        return Result.success(userDTO);
    }

    @PostMapping("/register")
    public Result<UserDTO> register(@RequestBody RegisterRequest req) {
        UserDTO userDTO = userService.register(req.getAccount(), req.getPassword(), req.getNickname(), req.getPhone());
        return Result.success(userDTO);
    }

    @GetMapping("/{id}")
    public Result<UserDTO> getUserInfo(@PathVariable Integer id) {
        UserDTO userDTO = userService.findById(id);
        return Result.success(userDTO);
    }

    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success("退出成功");
    }

    @PostMapping("/update")
    public Result<UserDTO> update(@RequestBody User user) {
        UserDTO userDTO = userService.update(user);
        return Result.success(userDTO);
    }
}
