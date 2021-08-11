package com.tenniscourts.guests;

import com.tenniscourts.TennisCourtApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { TennisCourtApplication.class })
@Transactional
public class GuestServiceTests {

  @MockBean
  private GuestRepository guestRepository;

  @Autowired
  private GuestService guestService;

  private GuestDTO guestDTO;

  private Guest guest;


  @BeforeEach
  public void setup() {
    guestDTO = GuestDTO.builder().name("test").build();
    guest = Guest.builder().name("test").build();

  }

  @Test
  public void createGuest() {
    Mockito.when(guestRepository.save(guest)).thenReturn(guest);
    GuestDTO dto = guestService.createGuest(guestDTO);

    Assert.assertEquals("test1", dto.getName());
  }
}
