package com.heavenfilms.backend.service.impl;

import com.heavenfilms.backend.dto.UserDTO;
import com.heavenfilms.backend.entity.User;
import com.heavenfilms.backend.exception.ServiceException;
import com.heavenfilms.backend.repository.UserRepository;
import com.heavenfilms.backend.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDTO login(String account, String password) {
        User user = userRepository.findByAccount(account);
        if (user == null || !user.getPassword().equals(password)) {
            throw new ServiceException("账号或密码错误");
        }
        return convertToDTO(user);
    }

    @Override
    public UserDTO register(String account, String password, String nickname, String phone) {
        if (userRepository.findByAccount(account) != null) {
            throw new ServiceException("账号已存在");
        }

        User newUser = new User();
        newUser.setAccount(account);
        newUser.setPassword(password);
        String finalNickname = (nickname != null && !nickname.isEmpty()) ? nickname : account;
        newUser.setNickname(finalNickname);
        newUser.setPhone(phone);
        
        User savedUser = userRepository.save(newUser);
        return convertToDTO(savedUser);
    }

    @Override
    public UserDTO update(User user) {
        if (user.getId() == null) {
            throw new ServiceException("用户ID不能为空");
        }
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new ServiceException("用户不存在"));
        
        if (user.getNickname() != null) existingUser.setNickname(user.getNickname());
        if (user.getPhone() != null) existingUser.setPhone(user.getPhone());
        if (user.getAvatar() != null) existingUser.setAvatar(user.getAvatar());
        if (user.getBio() != null) existingUser.setBio(user.getBio());
        
        User savedUser = userRepository.save(existingUser);
        return convertToDTO(savedUser);
    }

    @Override
    public UserDTO findById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ServiceException("用户不存在"));
        return convertToDTO(user);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }
}
