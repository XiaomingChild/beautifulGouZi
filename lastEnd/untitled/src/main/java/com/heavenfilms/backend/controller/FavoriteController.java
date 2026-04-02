package com.heavenfilms.backend.controller;

import com.heavenfilms.backend.common.Result;
import com.heavenfilms.backend.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/toggle")
    public Result<List<Integer>> toggle(@RequestBody Map<String, Integer> payload) {
        Integer userId = payload.get("userId");
        Integer movieId = payload.get("movieId");
        List<Integer> list = favoriteService.toggle(userId, movieId);
        return Result.success(list);
    }

    @GetMapping("/user/{userId}")
    public Result<List<Integer>> getUserFavoriteMovieIds(@PathVariable Integer userId) {
        List<Integer> list = favoriteService.getUserFavoriteMovieIds(userId);
        return Result.success(list);
    }
}
