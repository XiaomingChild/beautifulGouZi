package com.heavenfilms.backend.service;

import com.heavenfilms.backend.entity.Schedule;

import java.util.List;
import java.util.Map;

public interface ScheduleService {
    List<Map<String, Object>> getSchedulesByMovie(Integer movieId);
    Schedule getScheduleById(Integer id);
}
