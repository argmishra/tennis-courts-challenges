package com.tenniscourts.guests;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("guests")
@RequiredArgsConstructor
@Validated
public class GuestController  extends BaseRestController {

  private final GuestService guestService;

  @ApiOperation(value = "/guests", httpMethod = "POST", notes = "Create Guest" )
  @ApiResponses(value = {@ApiResponse(code = 201, message = "Guest Created", response = GuestDTO.class)})
  @PostMapping
  public ResponseEntity<GuestDTO> createGuest(@ApiParam(name = "guestDTO", value = "Guest Request Body", required = true) @RequestBody @Valid GuestDTO guestDTO,
                                              @ApiParam(name = "headers", value = "Http Headers", required = true) @RequestHeader HttpHeaders headers) {
    log.info("Crete new guest {} " ,guestDTO);
    this.checkForAdmin(headers);
    return new ResponseEntity(guestService.createGuest(guestDTO), HttpStatus.CREATED);
  }

  @ApiOperation(value = "/guests/{guestId}", httpMethod = "PUT", notes = "Update Guest" )
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Guest Updated", response = GuestDTO.class)})
  @PutMapping(value = "{guestId}")
  public ResponseEntity<GuestDTO> updateGuest( @ApiParam(name = "guestId", value = "Guest Id", required = true) @PathVariable Long guestId,
                                               @ApiParam(name = "guestDTO", value = "Guest Request Body", required = true) @RequestBody @Valid GuestDTO guestDTO,
                                               @ApiParam(name = "headers", value = "Http Headers", required = true) @RequestHeader HttpHeaders headers) {
    log.info("Update guest by id {}", guestId);
    this.checkForAdmin(headers);
    return new ResponseEntity(guestService.updateGuest(guestId, guestDTO), HttpStatus.OK);
  }

  @ApiOperation(value = "/guests/{guestId}", httpMethod = "GET", notes = "Find Guest" )
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Guest find", response = GuestDTO.class)})
  @GetMapping(value = "{guestId}")
  public ResponseEntity<GuestDTO> findGuest(@ApiParam(name = "guestId", value = "Guest Id", required = true) @PathVariable Long guestId,
                                            @ApiParam(name = "headers", value = "Http Headers", required = true) @RequestHeader HttpHeaders headers) {
    log.info("Find guest by id {}", guestId);
    this.checkForAdmin(headers);
    return ResponseEntity.ok(guestService.findGuest(guestId));
  }

  @ApiOperation(value = "/guests/{guestId}", httpMethod = "DELETE", notes = "Delete Guest" )
  @ApiResponses(value = {@ApiResponse(code = 204, message = "Guest Deleted")})
  @DeleteMapping(value = "{guestId}")
  public ResponseEntity<Void> deleteGuest(@ApiParam(name = "guestId", value = "Guest Id", required = true) @PathVariable Long guestId,
                                          @ApiParam(name = "headers", value = "Http Headers", required = true) @RequestHeader HttpHeaders headers) {
    log.info("Delete guest by id {}", guestId);
    this.checkForAdmin(headers);
    guestService.deleteGuest(guestId);
    return ResponseEntity.noContent().build();
  }

  @ApiOperation(value = "/guests", httpMethod = "GET", notes = "Find All" )
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Find guest all or by name", response = GuestDTO.class, responseContainer = "List")})
  @GetMapping
  public ResponseEntity<List<GuestDTO>> findAll(@ApiParam(name = "name", value = "Guest name", required = false) @RequestParam(name = "name", required = false) String name,
                                                @ApiParam(name = "headers", value = "Http Headers", required = true) @RequestHeader HttpHeaders headers) {
    log.info("Find all guest");
    this.checkForAdmin(headers);
    return ResponseEntity.ok(guestService.findAll(name));
  }


  private void checkForAdmin(HttpHeaders headers){
    if (!headers.get("user").get(0).equals("admin")){
      throw new UnsupportedOperationException();
    }
  }

}
