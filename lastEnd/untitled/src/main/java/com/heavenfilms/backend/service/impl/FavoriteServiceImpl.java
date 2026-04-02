package com.heavenfilms.backend.service.impl;

import com.heavenfilms.backend.entity.User;
import com.heavenfilms.backend.exception.ServiceException;
import com.heavenfilms.backend.repository.UserRepository;
import com.heavenfilms.backend.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Integer> toggle(Integer userId, Integer movieId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("用户不存在"));
        
        if (user.getSelected() == null) {
            user.setSelected(new ArrayList<>());
        }
        
        List<Integer> list = user.getSelected();
        if (list.contains(movieId)) {
            list.remove(movieId);
        } else {
            list.add(movieId);
        }
        userRepository.save(user);
        return list;
    }

    @Override
    public List<Integer> getUserFavoriteMovieIds(Integer userId) {
        return userRepository.findById(userId)
                .map(u -> u.getSelected() != null ? u.getSelected() : new ArrayList<Integer>())
                .orElse(new ArrayList<>());
    }
}
