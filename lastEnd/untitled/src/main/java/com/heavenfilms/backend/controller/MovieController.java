package com.heavenfilms.backend.controller;

import com.heavenfilms.backend.common.Result;
import com.heavenfilms.backend.entity.Movie;
import com.heavenfilms.backend.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/ranking")
    public Result<List<Movie>> getRanking() {
        return Result.success(movieService.getRanking());
    }

    @GetMapping("/hot")
    public Result<Page<Movie>> getHotMovies(
            @RequestParam(defaultValue = "all") String genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(movieService.getHotMovies(genre, page, size));
    }

    @GetMapping("/upcoming")
    public Result<Page<Movie>> getUpcomingMovies(
            @RequestParam(defaultValue = "all") String genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(movieService.getUpcomingMovies(genre, page, size));
    }

    @GetMapping("/category/{tag}")
    public Result<Page<Movie>> getMoviesByTag(
            @PathVariable String tag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(movieService.getMoviesByTag(tag, page, size));
    }

    @GetMapping("/{id}")
    public Result<Movie> getMovieById(@PathVariable Long id) {
        return Result.success(movieService.getMovieById(id));
    }

    @GetMapping("/all")
    public Result<List<Movie>> getAllMovies() {
        return Result.success(movieService.getAllMovies());
    }

    @PostMapping("/list")
    public Result<List<Movie>> getMoviesByIds(@RequestBody List<Long> ids) {
        return Result.success(movieService.getMoviesByIds(ids));
    }
}
