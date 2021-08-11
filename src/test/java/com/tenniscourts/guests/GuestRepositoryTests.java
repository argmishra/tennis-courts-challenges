package com.tenniscourts.guests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Optional;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class GuestRepositoryTests {

  @Autowired
  private GuestRepository guestRepository;

  private Guest guest;

  @Test
  public void findByName_success() {
    guest = Guest.builder().name("test").build();
    System.out.println(guestRepository);

    guestRepository.save(guest);

    Optional<Guest> guest = guestRepository.findByName("test");
    Assert.assertEquals("test", guest.get().getName());

  }

}
