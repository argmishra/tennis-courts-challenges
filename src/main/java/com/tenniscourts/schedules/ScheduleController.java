package com.tenniscourts.schedules;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("schedules")
@RequiredArgsConstructor
@Validated
public class ScheduleController extends BaseRestController {

    private final ScheduleService scheduleService;

    @ApiOperation(value = "/schedules", httpMethod = "POST", notes = "Add Schedule Tennis Court" )
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Schedule Added")})
    @PostMapping
    public ResponseEntity<Void> addScheduleTennisCourt(@ApiParam(name = "createScheduleRequestDTO", value = "Schedule Request Body", required = true) @RequestBody @Valid CreateScheduleRequestDTO createScheduleRequestDTO) {
        log.info("Create schedule from tennisCourtId {} " ,createScheduleRequestDTO.getTennisCourtId());
        return ResponseEntity.created(locationByEntity(scheduleService.addSchedule(createScheduleRequestDTO.getTennisCourtId(), createScheduleRequestDTO).getId())).build();
    }

    @ApiOperation(value = "/schedules", httpMethod = "GET", notes = "Find Schedule by Date Range" )
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Schedule Found", response = ScheduleDTO.class)})
    @GetMapping
    public ResponseEntity<List<ScheduleDTO>> findSchedulesByDates(@ApiParam(name = "startDate", value = "Start Date", required = true) @RequestParam(required = true, name = "startDate") LocalDate startDate,
                                                                  @ApiParam(name = "endDate", value = "End Date", required = true) @RequestParam(required = true, name = "endDate") LocalDate endDate) {
        log.info("Find schedule starting from {} and ending at {} ", startDate, endDate);
        return ResponseEntity.ok(scheduleService.findSchedulesByDates(LocalDateTime.of(startDate, LocalTime.of(0, 0)), LocalDateTime.of(endDate, LocalTime.of(23, 59))));
    }

    @ApiOperation(value = "/schedules/{scheduleId}", httpMethod = "GET", notes = "Find Schedule by Id" )
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Schedule Found", response = ScheduleDTO.class)})
    @GetMapping(value = "{scheduleId}")
    public ResponseEntity<ScheduleDTO> findByScheduleId(@ApiParam(name = "scheduleId", value = "Schedule Id", required = true) @PathVariable Long scheduleId) {
        log.info("Find schedule by id {}", scheduleId);
        return ResponseEntity.ok(scheduleService.findSchedule(scheduleId));
    }
}
