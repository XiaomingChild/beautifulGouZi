package com.heavenfilms.backend.repository;

import com.heavenfilms.backend.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    Optional<Favorite> findByUserIdAndMovieId(Integer userId, Integer movieId);
    List<Favorite> findByUserId(Integer userId);
}
