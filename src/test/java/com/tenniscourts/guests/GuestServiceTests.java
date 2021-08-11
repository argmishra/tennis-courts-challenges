package com.tenniscourts.guests;

import com.tenniscourts.exceptions.AlreadyExistsEntityException;
import com.tenniscourts.exceptions.EntityNotFoundException;
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
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = GuestService.class)
public class GuestServiceTests {

  @Mock
  private GuestRepository guestRepository;

  @Mock
  private GuestMapper guestMapper;

  @InjectMocks
  private GuestService guestService;

  private GuestDTO guestDTO;

  private Guest guest;

  private final String name = "test";

  private final Long id = 1l;

  @Before
  public void setup() {
    guestDTO = GuestDTO.builder().name(name).id(id).build();
    guest = Guest.builder().name(name).build();
  }

  @Test
  public void createGuest_success() {
    Mockito.when(guestRepository.findByName(name)).thenReturn(Optional.empty());
    Mockito.when(guestRepository.saveAndFlush(guest)).thenReturn(guest);
    Mockito.when(guestMapper.map(guest)).thenReturn(guestDTO);
    Mockito.when(guestMapper.map(guestDTO)).thenReturn(guest);

    GuestDTO dto = guestService.createGuest(guestDTO);

    Assert.assertEquals(name, dto.getName());
  }

  @Test
  public void createGuest_fail_guest_exists() {
    Mockito.when(guestRepository.findByName(name)).thenReturn(Optional.of(guest));

    assertThrows(AlreadyExistsEntityException.class, () -> {
      guestService.createGuest(guestDTO);
    });
  }

  @Test
  public void findGuest_success() {
    Mockito.when(guestRepository.findById(id)).thenReturn(Optional.of(guest));
    Mockito.when(guestMapper.map(guest)).thenReturn(guestDTO);

    GuestDTO dto = guestService.findGuest(id);

    Assert.assertEquals(id, dto.getId());
  }

  @Test
  public void findGuest_fail() {
    Mockito.when(guestRepository.findById(id)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> {
      guestService.findGuest(id);
    });
  }

  @Test
  public void deleteGuest_success() {
    guestService.deleteGuest(id);
    verify(guestRepository, times(1)).deleteById(id);
  }

  @Test
  public void findAll_success() {
    Mockito.when(guestRepository.findAll()).thenReturn(List.of(guest));
    Mockito.when(guestMapper.map(List.of(guest))).thenReturn(List.of(guestDTO));

    List<GuestDTO> dtoList = guestService.findAll(null);

    Assert.assertEquals(1, dtoList.size());
  }

  @Test
  public void findByName_success() {
    Mockito.when(guestRepository.findByName(name)).thenReturn(Optional.of(guest));
    Mockito.when(guestMapper.map(List.of(guest))).thenReturn(List.of(guestDTO));

    List<GuestDTO> dtoList = guestService.findAll(name);
    Assert.assertEquals(name, dtoList.get(0).getName());
  }


  @Test
  public void updateGuest_success() {
    GuestDTO newGuestDTO = GuestDTO.builder().name("newtest").id(id).build();
    Guest newGuest = Guest.builder().name("newtest").build();

    Mockito.when(guestRepository.findById(id)).thenReturn(Optional.of(guest));
    Mockito.when(guestMapper.map(guest)).thenReturn(guestDTO);
    Mockito.when(guestMapper.map(newGuestDTO)).thenReturn(guest);
    Mockito.when(guestRepository.save(guestMapper.map(newGuestDTO))).thenReturn(newGuest);
    Mockito.when(guestMapper.map(newGuest)).thenReturn(newGuestDTO);

    GuestDTO dto = guestService.updateGuest(id, newGuestDTO);

    Assert.assertEquals("newtest", dto.getName());
  }

}
