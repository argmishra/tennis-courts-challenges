package com.tenniscourts.guests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = GuestController.class)
public class GuestControllerTests {

  @Mock
  private GuestService guestService;

  @InjectMocks
  private GuestController guestController;

  private GuestDTO guestDTO;

  private final String name = "test";

  private final Long id = 1l;

  private HttpHeaders headers;

  @Before
  public void setup() {
    guestDTO = GuestDTO.builder().name(name).id(id).build();
    headers = new HttpHeaders();
    headers.add("user", "admin");
  }

  @Test
  public void createGuest_success() {
    Mockito.when(guestService.createGuest(guestDTO)).thenReturn(guestDTO);

    ResponseEntity<GuestDTO> dto = guestController.createGuest(guestDTO, headers);
    Assert.assertEquals(name, dto.getBody().getName());
  }

  @Test
  public void updateGuest_success() {
    Mockito.when(guestService.updateGuest(id, guestDTO)).thenReturn(guestDTO);

    ResponseEntity<GuestDTO> dto = guestController.updateGuest(id, guestDTO, headers);
    Assert.assertEquals(name, dto.getBody().getName());
  }

  @Test
  public void findGuest_success() {
    Mockito.when(guestService.findGuest(id)).thenReturn(guestDTO);

    ResponseEntity<GuestDTO> dto = guestController.findGuest(id, headers);
    Assert.assertEquals(name, dto.getBody().getName());
  }

  @Test
  public void deleteGuest_success() {
    guestController.deleteGuest(id, headers);
    verify(guestService, times(1)).deleteGuest(id);
  }

  @Test
  public void findAllGuest_success() {
    Mockito.when(guestService.findAll(null)).thenReturn(List.of(guestDTO));

    ResponseEntity<List<GuestDTO>> list= guestController.findAll(null, headers);
    Assert.assertEquals(1, list.getBody().size());
  }

  @Test
  public void findGuestByName_success() {
    Mockito.when(guestService.findAll(name)).thenReturn(List.of(guestDTO));

    ResponseEntity<List<GuestDTO>> list= guestController.findAll(name, headers);
    Assert.assertEquals(name, list.getBody().get(0).getName());
  }

  @Test
  public void invalid_header() {
    HttpHeaders newheaders = new HttpHeaders();
    newheaders.add("user", "invalid");

    assertThrows(UnsupportedOperationException.class, () -> {
      guestController.deleteGuest(id, newheaders);
    });
  }


}
