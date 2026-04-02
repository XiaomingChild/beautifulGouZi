package com.heavenfilms.backend.controller;

import com.heavenfilms.backend.entity.Movie;
import com.heavenfilms.backend.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping("/ranking")
    public List<Movie> getRanking() {
        return movieRepository.findAll(Sort.by(Sort.Direction.DESC, "rating")).stream()
                .limit(10)
                .collect(Collectors.toList());
    }

    @GetMapping("/hot")
    public Page<Movie> getHotMovies(
            @RequestParam(defaultValue = "all") String genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return movieRepository.findHotMovies(new Date(), genre, 
                PageRequest.of(page, size, Sort.by("releaseDate").descending()));
    }

    @GetMapping("/upcoming")
    public Page<Movie> getUpcomingMovies(
            @RequestParam(defaultValue = "all") String genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return movieRepository.findUpcomingMovies(new Date(), genre, 
                PageRequest.of(page, size, Sort.by("releaseDate").ascending()));
    }

    @GetMapping("/category/{tag}")
    public Page<Movie> getMoviesByTag(
            @PathVariable String tag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return movieRepository.findHotMovies(new Date(), tag, PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public Movie getMovieById(@PathVariable Integer id) {
        return movieRepository.findById(id).orElse(null);
    }

    @GetMapping("/all")
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @PostMapping("/list")
    public List<Movie> getMoviesByIds(@RequestBody List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return new java.util.ArrayList<>();
        return movieRepository.findAllById(ids);
    }
}
