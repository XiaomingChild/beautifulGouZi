package com.heavenfilms.backend.service;

import java.util.List;

public interface FavoriteService {
    List<Long> toggle(Long userId, Long movieId);
    List<Long> getUserFavoriteMovieIds(Long userId);
}
