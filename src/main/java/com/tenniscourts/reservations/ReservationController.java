package com.tenniscourts.reservations;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("reservations")
@RequiredArgsConstructor
@Validated
public class ReservationController extends BaseRestController {

    private final ReservationService reservationService;

    @ApiOperation(value = "/reservations", httpMethod = "POST", notes = "Book Reservation")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Schedule Added")})
    @PostMapping
    public ResponseEntity<Void> bookReservation(@ApiParam(name = "createReservationRequestDTO", value = "Reservation Request Body", required = true) @RequestBody  @Valid CreateReservationRequestDTO createReservationRequestDTO) {
        log.info("Book reservation for guestId {} " ,createReservationRequestDTO.getGuestId());
        return ResponseEntity.created(locationByEntity(reservationService.bookReservation(createReservationRequestDTO).getId())).build();
    }

    @ApiOperation(value = "/reservations/{reservationId}", httpMethod = "GET", notes = "Find Reservation")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Reservation find", response = ReservationDTO.class)})
    @GetMapping(value = "{reservationId}")
    public ResponseEntity<ReservationDTO> findReservation(@ApiParam(name = "reservationId", value = "Reservation Id", required = true) @PathVariable Long reservationId) {
        log.info("Find reservation by id {}", reservationId);
        return ResponseEntity.ok(reservationService.findReservation(reservationId));
    }

    @ApiOperation(value = "/reservations/{reservationId}", httpMethod = "DELETE", notes = "Cancel Reservation")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Reservation cancelled", response = ReservationDTO.class)})
    @DeleteMapping(value = "{reservationId}")
    public ResponseEntity<ReservationDTO> cancelReservation(@ApiParam(name = "reservationId", value = "Reservation Id", required = true) @PathVariable Long reservationId) {
        log.info("Delete reservation by id {}", reservationId);
        return ResponseEntity.ok(reservationService.cancelReservation(reservationId));
    }

    @ApiOperation(value = "/reservations", httpMethod = "PUT", notes = "Reschedule Reservation")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Reservation Rescheduled", response = ReservationDTO.class)})
    @PutMapping
    public ResponseEntity<ReservationDTO> rescheduleReservation(@ApiParam(name = "reservationId", value = "Reservation Id", required = true) @RequestParam(required = true, name = "reservationId") Long reservationId,
                                                                @ApiParam(name = "scheduleId", value = "Schedule Id", required = true) @RequestParam(required = true, name = "scheduleId") Long scheduleId) {
        log.info("Reschedule for reservationId {} and scheduleId {} ", reservationId, scheduleId);
        return ResponseEntity.ok(reservationService.rescheduleReservation(reservationId, scheduleId));
    }
}
