package com.tenniscourts.guests;

import com.tenniscourts.exceptions.AlreadyExistsEntityException;
import com.tenniscourts.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GuestService {

  private final GuestRepository guestRepository;

  private final GuestMapper guestMapper;

  public GuestDTO createGuest(GuestDTO guestDTO) {
    this.findByName(guestDTO.getName());
    return guestMapper.map(guestRepository.saveAndFlush(guestMapper.map(guestDTO)));
  }

  public GuestDTO updateGuest(Long id, GuestDTO guestDTO) {
    GuestDTO dto = findGuest(id);
    dto.setName(guestDTO.getName());
    return guestMapper.map(guestRepository.save(guestMapper.map(dto)));
  }


  public GuestDTO findGuest(Long id) {
    return guestRepository.findById(id).map(guestMapper::map).orElseThrow(() -> {
      throw new EntityNotFoundException("Guest not found.");
    });
  }

  public void deleteGuest(Long id) {
    guestRepository.deleteById(id);
  }

  public List<GuestDTO> findAll(String name) {
    List<Guest> guests = new ArrayList<>();
    if(name == null) {
      guests = guestRepository.findAll();
    } else{
      guests.add(guestRepository.findByName(name).get());
    }

    return guestMapper.map(guests);

  }

  private Guest findByName(String name) {
     return guestRepository.findByName(name).orElseThrow(() -> {
      throw new AlreadyExistsEntityException("Guest exists.");
    });
  }



}
