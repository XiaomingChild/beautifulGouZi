package com.heavenfilms.backend.repository;

import com.heavenfilms.backend.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    
    @Query("SELECT m FROM Movie m WHERE m.isShowing = 1 AND (:genre = 'all' OR m.genre LIKE %:genre%)")
    Page<Movie> findHotMovies(@Param("genre") String genre, Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE m.isShowing = 0 AND (:genre = 'all' OR m.genre LIKE %:genre%)")
    Page<Movie> findUpcomingMovies(@Param("genre") String genre, Pageable pageable);
    
    Page<Movie> findByTitleContaining(String title, Pageable pageable);

    @Query("SELECT m FROM Movie m ORDER BY m.rating DESC")
    java.util.List<Movie> findTop10ByOrderByRatingDesc();
}
