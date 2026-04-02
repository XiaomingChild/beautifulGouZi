package com.heavenfilms.backend.controller;

import com.heavenfilms.backend.entity.User;
import com.heavenfilms.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin
public class FavoriteController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/toggle")
    public List<Integer> toggle(@RequestBody java.util.Map<String, Integer> payload) {
        Integer userId = payload.get("userId");
        Integer movieId = payload.get("movieId");

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getSelected() == null) user.setSelected(new ArrayList<>());
            
            List<Integer> list = user.getSelected();
            if (list.contains(movieId)) {
                list.remove(movieId);
            } else {
                list.add(movieId);
            }
            userRepository.save(user);
            return list;
        }
        return new ArrayList<>();
    }

    @GetMapping("/user/{userId}")
    public List<Integer> getUserFavoriteMovieIds(@PathVariable Integer userId) {
        return userRepository.findById(userId)
                .map(u -> u.getSelected() != null ? u.getSelected() : new ArrayList<Integer>())
                .orElse(new ArrayList<>());
    }
}
