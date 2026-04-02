package com.heavenfilms.backend.controller;

import com.heavenfilms.backend.entity.Cinema;
import com.heavenfilms.backend.entity.Schedule;
import com.heavenfilms.backend.repository.CinemaRepository;
import com.heavenfilms.backend.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/schedules")
@CrossOrigin
public class ScheduleController {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private CinemaRepository cinemaRepository;

    /**
     * 获取指定电影在各影院的排片情况
     * 返回结构：List<{ cinema: Cinema, schedules: List<Schedule> }>
     */
    @GetMapping("/movie/{movieId}")
    public List<Map<String, Object>> getSchedulesByMovie(@PathVariable Integer movieId) {
        List<Schedule> allSchedules = scheduleRepository.findByMovieIdOrderByStartTimeAsc(movieId);

        // 按影院 ID 分组
        Map<Integer, List<Schedule>> groupedByCinema = allSchedules.stream()
                .collect(Collectors.groupingBy(Schedule::getCinemaId));

        return groupedByCinema.entrySet().stream().map(entry -> {
            Map<String, Object> map = new HashMap<>();
            Cinema cinema = cinemaRepository.findById(entry.getKey()).orElse(null);
            map.put("cinema", cinema);
            map.put("schedules", entry.getValue());
            return map;
        }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Schedule getScheduleById(@PathVariable Integer id) {
        return scheduleRepository.findById(id).orElse(null);
    }
}
