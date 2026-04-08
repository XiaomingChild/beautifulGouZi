package com.heavenfilms.backend.service.impl;

import com.heavenfilms.backend.entity.Cinema;
import com.heavenfilms.backend.entity.Schedule;
import com.heavenfilms.backend.repository.CinemaRepository;
import com.heavenfilms.backend.repository.ScheduleRepository;
import com.heavenfilms.backend.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private CinemaRepository cinemaRepository;

    @Override
    public List<Map<String, Object>> getSchedulesByMovie(Long movieId) {
        List<Schedule> allSchedules = scheduleRepository.findByMovieIdOrderByStartTimeAsc(movieId);

        Map<Long, List<Schedule>> groupedByCinema = allSchedules.stream()
                .collect(Collectors.groupingBy(Schedule::getCinemaId));

        return groupedByCinema.entrySet().stream().map(entry -> {
            Map<String, Object> map = new HashMap<>();
            Cinema cinema = cinemaRepository.findById(entry.getKey()).orElse(null);
            map.put("cinema", cinema);
            map.put("schedules", entry.getValue());
            return map;
        }).collect(Collectors.toList());
    }

    @Override
    public Schedule getScheduleById(Long id) {
        return scheduleRepository.findById(id).orElse(null);
    }
}
