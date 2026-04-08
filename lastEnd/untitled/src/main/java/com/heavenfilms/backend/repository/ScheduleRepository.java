package com.heavenfilms.backend.repository;

import com.heavenfilms.backend.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    // 根据电影ID查询所有排片场次
    java.util.List<Schedule> findByMovieIdOrderByStartTimeAsc(Long movieId);
    
    // 根据影院ID查询所有排片
    java.util.List<Schedule> findByCinemaIdOrderByShowDateAscStartTimeAsc(Long cinemaId);
}
