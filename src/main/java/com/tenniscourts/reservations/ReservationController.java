package com.tenniscourts.reservations;

import com.tenniscourts.config.BaseRestController;
import lombok.AllArgsConstructor;
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
// Include court id
// Book Not working
public class ReservationController extends BaseRestController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<Void> bookReservation(@RequestBody CreateReservationRequestDTO createReservationRequestDTO) {
        log.info("Book reservation for guestId {} " ,createReservationRequestDTO.getGuestId());
        return ResponseEntity.created(locationByEntity(reservationService.bookReservation(createReservationRequestDTO).getId())).build();
    }

    @GetMapping(value = "{reservationId}")
    public ResponseEntity<ReservationDTO> findReservation(@PathVariable Long reservationId) {
        log.info("Find reservation by id {}", reservationId);
        return ResponseEntity.ok(reservationService.findReservation(reservationId));
    }

    @DeleteMapping(value = "{reservationId}")
    public ResponseEntity<ReservationDTO> cancelReservation(Long reservationId) {
        log.info("Delete reservation by id {}", reservationId);
        return ResponseEntity.ok(reservationService.cancelReservation(reservationId));
    }

    @PutMapping
    public ResponseEntity<ReservationDTO> rescheduleReservation(@RequestParam(required = true, name = "reservationId") Long reservationId,
                                                                @RequestParam(required = true, name = "scheduleId") Long scheduleId) {
        log.info("Reschedule for reservationId {} and scheduleId {} ", reservationId, scheduleId);
        return ResponseEntity.ok(reservationService.rescheduleReservation(reservationId, scheduleId));
    }
}
