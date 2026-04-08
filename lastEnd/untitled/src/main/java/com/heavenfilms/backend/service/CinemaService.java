package com.heavenfilms.backend.service;

import com.heavenfilms.backend.entity.Cinema;
import java.util.List;

public interface CinemaService {
    java.util.List<Cinema> getAllCinemas();
    Cinema getCinemaById(Long id);
}
