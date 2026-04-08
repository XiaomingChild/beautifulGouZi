package com.heavenfilms.backend.service.impl;

import com.heavenfilms.backend.entity.Favorite;
import com.heavenfilms.backend.repository.FavoriteRepository;
import com.heavenfilms.backend.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Override
    @Transactional
    public List<Long> toggle(Long userId, Long movieId) {
        Optional<Favorite> favoriteOpt = favoriteRepository.findByUserIdAndMovieId(userId, movieId);
        if (favoriteOpt.isPresent()) {
            favoriteRepository.delete(favoriteOpt.get());
        } else {
            Favorite favorite = new Favorite();
            favorite.setUserId(userId);
            favorite.setMovieId(movieId);
            favoriteRepository.save(favorite);
        }
        return getUserFavoriteMovieIds(userId);
    }

    @Override
    public List<Long> getUserFavoriteMovieIds(Long userId) {
        return favoriteRepository.findByUserId(userId).stream()
                .map(Favorite::getMovieId)
                .collect(Collectors.toList());
    }
}
