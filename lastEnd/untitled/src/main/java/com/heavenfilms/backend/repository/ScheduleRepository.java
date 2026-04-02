package com.heavenfilms.backend.repository;

import com.heavenfilms.backend.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    // 根据电影ID查询所有排片场次
    List<Schedule> findByMovieIdOrderByStartTimeAsc(Integer movieId);
}
