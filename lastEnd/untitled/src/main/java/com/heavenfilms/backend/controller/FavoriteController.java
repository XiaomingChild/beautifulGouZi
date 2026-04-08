package com.heavenfilms.backend.controller;

import com.heavenfilms.backend.common.Result;
import com.heavenfilms.backend.service.FavoriteService;
import lombok.Data;
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

    @Data
    public static class FavoriteRequest {
        private Long userId;
        private Long movieId;
    }

    @PostMapping("/toggle")
    public Result<List<Long>> toggle(@RequestBody FavoriteRequest req) {
        List<Long> list = favoriteService.toggle(req.getUserId(), req.getMovieId());
        return Result.success(list);
    }

    @GetMapping("/user/{userId}")
    public Result<List<Long>> getUserFavoriteMovieIds(@PathVariable Long userId) {
        List<Long> list = favoriteService.getUserFavoriteMovieIds(userId);
        return Result.success(list);
    }
}
