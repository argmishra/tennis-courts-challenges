package com.tenniscourts.tenniscourts;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("courts")
@RequiredArgsConstructor
@Validated
public class TennisCourtController extends BaseRestController {

    private final TennisCourtService tennisCourtService;

    @ApiOperation(value = "/courts", httpMethod = "POST", notes = "Create Court" )
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Court Created")})
    @PostMapping
    public ResponseEntity<Void> addTennisCourt(@ApiParam(name = "tennisCourtDTO", value = "Court Request Body", required = true) @RequestBody @Valid TennisCourtDTO tennisCourtDTO) {
        log.info("Adding new tennis court");
        return ResponseEntity.created(locationByEntity(tennisCourtService.addTennisCourt(tennisCourtDTO).getId())).build();
    }

    @ApiOperation(value = "/courts/{tennisCourtId}", httpMethod = "GET", notes = "Find Court" )
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Court Found", response = TennisCourtDTO.class)})
    @GetMapping(value = "{tennisCourtId}")
    public ResponseEntity<TennisCourtDTO> findTennisCourtById(@ApiParam(name = "tennisCourtId", value = "Court Id", required = true) @PathVariable Long tennisCourtId) {
        log.info("Find tennis court by id {}", tennisCourtId);
        return ResponseEntity.ok(tennisCourtService.findTennisCourtById(tennisCourtId));
    }

    @ApiOperation(value = "/courts/{tennisCourtId}/schedules", httpMethod = "GET", notes = "Find Court by Schedule Id" )
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Court found", response = TennisCourtDTO.class)})
    @GetMapping(value = "{tennisCourtId}/schedules")
    public ResponseEntity<TennisCourtDTO> findTennisCourtWithSchedulesById(@ApiParam(name = "tennisCourtId", value = "Court Id", required = true) @PathVariable Long tennisCourtId) {
        log.info("Find schedules with tennis court id {}", tennisCourtId);
        return ResponseEntity.ok(tennisCourtService.findTennisCourtWithSchedulesById(tennisCourtId));
    }
}
