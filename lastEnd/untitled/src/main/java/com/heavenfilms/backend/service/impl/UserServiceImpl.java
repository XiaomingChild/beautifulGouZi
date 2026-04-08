package com.heavenfilms.backend.service.impl;

import com.heavenfilms.backend.dto.UserDTO;
import com.heavenfilms.backend.entity.User;
import com.heavenfilms.backend.exception.ServiceException;
import com.heavenfilms.backend.repository.UserRepository;
import com.heavenfilms.backend.service.FavoriteService;
import com.heavenfilms.backend.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FavoriteService favoriteService;

    @Override
    public UserDTO login(String account, String password) {
        User user = userRepository.findByAccount(account)
                .orElseThrow(() -> new ServiceException("账号或密码错误"));
        
        // 核心修改：直接比对明文密码
        if (!password.equals(user.getPassword())) {
            throw new ServiceException("账号或密码错误");
        }
        return convertToDTO(user);
    }

    @Override
    public UserDTO register(String account, String password, String nickname, String phone) {
        if (userRepository.findByAccount(account).isPresent()) {
            throw new ServiceException("账号已存在");
        }

        User newUser = new User();
        newUser.setAccount(account);
        // 核心修改：直接存储明文密码
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
        if (user.getAvatarUrl() != null) existingUser.setAvatarUrl(user.getAvatarUrl());
        if (user.getBio() != null) existingUser.setBio(user.getBio());
        
        // 如果有密码修改需求，这里也应保持明文
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(user.getPassword());
        }
        
        User savedUser = userRepository.save(existingUser);
        return convertToDTO(savedUser);
    }

    @Override
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ServiceException("用户不存在"));
        return convertToDTO(user);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(user, dto);
        dto.setFavoriteMovieIds(favoriteService.getUserFavoriteMovieIds(user.getId()));
        return dto;
    }
}
