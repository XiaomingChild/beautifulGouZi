package com.heavenfilms.backend.controller;

import com.heavenfilms.backend.common.Result;
import com.heavenfilms.backend.entity.Schedule;
import com.heavenfilms.backend.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/schedules")
@CrossOrigin
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/movie/{movieId}")
    public Result<List<Map<String, Object>>> getSchedulesByMovie(@PathVariable Integer movieId) {
        List<Map<String, Object>> schedules = scheduleService.getSchedulesByMovie(movieId);
        return Result.success(schedules);
    }

    @GetMapping("/{id}")
    public Result<Schedule> getScheduleById(@PathVariable Integer id) {
        Schedule schedule = scheduleService.getScheduleById(id);
        return Result.success(schedule);
    }
}
