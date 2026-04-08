package com.heavenfilms.backend.service;

import com.heavenfilms.backend.entity.Movie;
import org.springframework.data.domain.Page;
import java.util.List;

public interface MovieService {
    List<Movie> getRanking();
    Page<Movie> getHotMovies(String genre, int page, int size);
    Page<Movie> getUpcomingMovies(String genre, int page, int size);
    Page<Movie> getMoviesByTag(String tag, int page, int size);
    Movie getMovieById(Long id);
    List<Movie> getAllMovies();
    List<Movie> getMoviesByIds(List<Long> ids);
}
