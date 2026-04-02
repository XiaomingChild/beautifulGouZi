package com.heavenfilms.backend.service;

import java.util.List;

public interface FavoriteService {
    List<Integer> toggle(Integer userId, Integer movieId);
    List<Integer> getUserFavoriteMovieIds(Integer userId);
}
