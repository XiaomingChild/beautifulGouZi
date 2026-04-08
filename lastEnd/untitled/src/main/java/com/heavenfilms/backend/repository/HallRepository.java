package com.heavenfilms.backend.repository;

import com.heavenfilms.backend.entity.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HallRepository extends JpaRepository<Hall, Long> {
    // 根据影院ID查找所有影厅
    List<Hall> findByCinemaId(Long cinemaId);
}
