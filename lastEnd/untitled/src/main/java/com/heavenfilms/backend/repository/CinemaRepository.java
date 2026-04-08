package com.heavenfilms.backend.repository;

import com.heavenfilms.backend.entity.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CinemaRepository extends JpaRepository<Cinema, Long> {
}
