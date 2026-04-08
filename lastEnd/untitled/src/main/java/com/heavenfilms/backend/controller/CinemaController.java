package com.heavenfilms.backend.controller;

import com.heavenfilms.backend.common.Result;
import com.heavenfilms.backend.entity.Cinema;
import com.heavenfilms.backend.service.CinemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cinemas")
@CrossOrigin
public class CinemaController {

    @Autowired
    private CinemaService cinemaService;

    @GetMapping("/all")
    public Result<List<Cinema>> getAllCinemas() {
        return Result.success(cinemaService.getAllCinemas());
    }

    @GetMapping("/{id}")
    public Result<Cinema> getCinemaById(@PathVariable Long id) {
        return Result.success(cinemaService.getCinemaById(id));
    }
}
