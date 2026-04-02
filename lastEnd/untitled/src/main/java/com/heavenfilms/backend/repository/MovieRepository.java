package com.heavenfilms.backend.repository;

import com.heavenfilms.backend.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
    
    @Query("SELECT m FROM Movie m WHERE m.releaseDate <= :now AND (:genre = 'all' OR m.genre LIKE %:genre%)")
    Page<Movie> findHotMovies(@Param("now") Date now, @Param("genre") String genre, Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE m.releaseDate > :now AND (:genre = 'all' OR m.genre LIKE %:genre%)")
    Page<Movie> findUpcomingMovies(@Param("now") Date now, @Param("genre") String genre, Pageable pageable);
    
    Page<Movie> findByTitleContaining(String title, Pageable pageable);
}
