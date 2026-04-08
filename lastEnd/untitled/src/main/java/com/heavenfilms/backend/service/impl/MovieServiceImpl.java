package com.heavenfilms.backend.service.impl;

import com.heavenfilms.backend.entity.Movie;
import com.heavenfilms.backend.repository.MovieRepository;
import com.heavenfilms.backend.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public List<Movie> getRanking() {
        return movieRepository.findAll(Sort.by(Sort.Direction.DESC, "rating")).stream()
                .limit(20)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Movie> getHotMovies(String genre, int page, int size) {
        return movieRepository.findHotMovies(genre, 
                PageRequest.of(page, size, Sort.by("rating").descending()));
    }

    @Override
    public Page<Movie> getUpcomingMovies(String genre, int page, int size) {
        return movieRepository.findUpcomingMovies(genre, 
                PageRequest.of(page, size, Sort.by("releaseDate").ascending()));
    }

    @Override
    public Page<Movie> getMoviesByTag(String tag, int page, int size) {
        return movieRepository.findHotMovies(tag, PageRequest.of(page, size));
    }

    @Override
    public Movie getMovieById(Long id) {
        return movieRepository.findById(id).orElse(null);
    }

    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public List<Movie> getMoviesByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return new ArrayList<>();
        return movieRepository.findAllById(ids);
    }
}
