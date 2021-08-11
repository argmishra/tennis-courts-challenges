package com.tenniscourts.guests;


import com.tenniscourts.config.BaseRestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("guests")
@RequiredArgsConstructor
@Validated
public class GuestController  extends BaseRestController {

  private final GuestService guestService;

  @PostMapping
  public ResponseEntity<GuestDTO> createGuest(@RequestBody GuestDTO guestDTO, @RequestHeader HttpHeaders headers) {
    log.info("Crete new guest {} " ,guestDTO);
    this.checkForAdmin(headers);
    return ResponseEntity.created(locationByEntity(guestService.createGuest(guestDTO).getId())).build();
  }

  @PutMapping(value = "{guestId}")
  public ResponseEntity<GuestDTO> updateGuest(@PathVariable Long guestId, @RequestBody GuestDTO guestDTO, @RequestHeader HttpHeaders headers) {
    log.info("Update guest by id {}", guestId);
    this.checkForAdmin(headers);
    return ResponseEntity.created(locationByEntity(guestService.updateGuest(guestId, guestDTO).getId())).build();
  }

  @GetMapping(value = "{guestId}")
  public ResponseEntity<GuestDTO> findGuest(@PathVariable Long guestId, @RequestHeader HttpHeaders headers) {
    log.info("Find guest by id {}", guestId);
    this.checkForAdmin(headers);
    return ResponseEntity.ok(guestService.findGuest(guestId));
  }

  @DeleteMapping(value = "{guestId}")
  public ResponseEntity<Void> deleteGuest(@PathVariable Long guestId, @RequestHeader HttpHeaders headers) {
    log.info("Delete guest by id {}", guestId);
    this.checkForAdmin(headers);
    guestService.deleteGuest(guestId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public ResponseEntity<List<GuestDTO>> findAll(@RequestParam(name = "name", required = false) String name, @RequestHeader HttpHeaders headers) {
    log.info("Find all guest");
    this.checkForAdmin(headers);
    return ResponseEntity.ok(guestService.findAll(name));
  }


  private void checkForAdmin(HttpHeaders headers){
    if (!headers.get("user").equals("admin")){
      throw new UnsupportedOperationException();
    }
  }

}
