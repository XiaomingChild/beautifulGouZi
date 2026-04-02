package com.heavenfilms.backend.service;

import com.heavenfilms.backend.dto.UserDTO;
import com.heavenfilms.backend.entity.User;

public interface UserService {
    UserDTO login(String account, String password);
    UserDTO register(String account, String password, String nickname, String phone);
    UserDTO update(User user);
    UserDTO findById(Integer id);
}
