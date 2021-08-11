package com.tenniscourts.schedules;

import com.tenniscourts.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final ScheduleMapper scheduleMapper;

    public ScheduleDTO addSchedule(Long tennisCourtId, CreateScheduleRequestDTO createScheduleRequestDTO) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setTennisCourtId(tennisCourtId);
        scheduleDTO.setStartDateTime(createScheduleRequestDTO.getStartDateTime());
        return scheduleMapper.map(scheduleRepository.saveAndFlush(scheduleMapper.map(scheduleDTO)));
    }

    public List<ScheduleDTO> findSchedulesByDates(LocalDateTime startDate, LocalDateTime endDate) {
        List<Schedule> scheduleList = scheduleRepository.findAllByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(startDate, endDate);
        Collections.sort(scheduleList, Comparator.comparing(Schedule::getId).reversed());
        return scheduleMapper.map(scheduleList);
    }

    public ScheduleDTO findSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId).map(scheduleMapper::map).orElseThrow(() -> {
            throw new EntityNotFoundException("Schedule not found.");
        });
    }

    public List<ScheduleDTO> findSchedulesByTennisCourtId(Long tennisCourtId) {
        return scheduleMapper.map(scheduleRepository.findByTennisCourt_IdOrderByStartDateTime(tennisCourtId));
    }
}
