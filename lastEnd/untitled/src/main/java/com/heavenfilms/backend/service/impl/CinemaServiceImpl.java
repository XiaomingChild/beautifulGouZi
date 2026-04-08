package com.heavenfilms.backend.service.impl;

import com.heavenfilms.backend.entity.Cinema;
import com.heavenfilms.backend.repository.CinemaRepository;
import com.heavenfilms.backend.service.CinemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CinemaServiceImpl implements CinemaService {

    @Autowired
    private CinemaRepository cinemaRepository;

    @Override
    public List<Cinema> getAllCinemas() {
        return cinemaRepository.findAll();
    }

    @Override
    public Cinema getCinemaById(Long id) {
        return cinemaRepository.findById(id).orElse(null);
    }
}
