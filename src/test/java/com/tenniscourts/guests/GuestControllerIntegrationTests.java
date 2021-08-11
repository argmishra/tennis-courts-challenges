package com.tenniscourts.guests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenniscourts.TennisCourtApplication;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TennisCourtApplication.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GuestControllerIntegrationTests {

  @Autowired
  private MockMvc mockMvc;

  ObjectMapper mapper = new ObjectMapper();

  private GuestDTO guestDTO;

  private final String name = "test";

  @Before
  public void setup()  {
    guestDTO = GuestDTO.builder().name(name).build();
  }

  @Test
  public void createGuest() throws Exception {
    MvcResult mvcResult = mockMvc.perform(post("/guests").header("user","admin").
            content(mapper.writeValueAsString(guestDTO)).
            contentType(MediaType.APPLICATION_JSON)).
                    andExpect(status().isCreated()).andReturn();
    GuestDTO guestDto = mapper.readValue(mvcResult.getResponse().getContentAsString(), GuestDTO.class);

    Assert.assertEquals(name, guestDto.getName());
  }

  @Test
  public void updateGuest() throws Exception {
    MvcResult mvcResult = mockMvc.
        perform(post("/guests").header("user","admin").
            content(mapper.writeValueAsString(guestDTO)).
            contentType(MediaType.APPLICATION_JSON)).
        andExpect(status().isCreated()).andReturn();
    GuestDTO guestDto = mapper.readValue(mvcResult.getResponse().getContentAsString(), GuestDTO.class);

    Long id = guestDto.getId();
    guestDTO = GuestDTO.builder().name("newtest").build();

    mvcResult =mockMvc.perform(put("/guests/" + id).contentType(MediaType.APPLICATION_JSON)
        .header("user","admin").content(mapper.writeValueAsString(guestDTO))).andExpect(status().isOk()).andReturn();
    guestDto = mapper.readValue(mvcResult.getResponse().getContentAsString(), GuestDTO.class);

    Assert.assertEquals("newtest", guestDto.getName());
  }

  @Test
  public void findGuest() throws Exception {
    MvcResult mvcResult = mockMvc.
        perform(post("/guests").header("user","admin").
            content(mapper.writeValueAsString(guestDTO)).
            contentType(MediaType.APPLICATION_JSON)).
        andExpect(status().isCreated()).andReturn();
    GuestDTO guestDto = mapper.readValue(mvcResult.getResponse().getContentAsString(), GuestDTO.class);

    Long id = guestDto.getId();

    mvcResult = mockMvc.perform(get("/guests/" + id).contentType(MediaType.APPLICATION_JSON)
        .header("user","admin")).andExpect(status().isOk()).andReturn();
    guestDto = mapper.readValue(mvcResult.getResponse().getContentAsString(), GuestDTO.class);

    Assert.assertEquals(name, guestDto.getName());
  }

  @Test
  public void deleteGuest() throws Exception {
    MvcResult mvcResult = mockMvc.perform(post("/guests").header("user","admin").
            content(mapper.writeValueAsString(guestDTO)).
            contentType(MediaType.APPLICATION_JSON)).
        andExpect(status().isCreated()).andReturn();
    GuestDTO guestDto = mapper.readValue(mvcResult.getResponse().getContentAsString(), GuestDTO.class);

    Long id = guestDto.getId();

    mockMvc.perform(delete("/guests/" + id).contentType(MediaType.APPLICATION_JSON)
        .header("user","admin")).andExpect(status().isNoContent());
  }

  @Test
  public void findAll() throws Exception {
    mockMvc.perform(post("/guests").header("user","admin").
            content(mapper.writeValueAsString(guestDTO)).
            contentType(MediaType.APPLICATION_JSON)).
        andExpect(status().isCreated()).andReturn();

    guestDTO = GuestDTO.builder().name("newtest").build();

    mockMvc.perform(post("/guests").header("user","admin").
            content(mapper.writeValueAsString(guestDTO)).
            contentType(MediaType.APPLICATION_JSON)).
        andExpect(status().isCreated()).andReturn();

    MvcResult mvcResult = mockMvc.perform(get("/guests").header("user","admin").
        contentType(MediaType.APPLICATION_JSON)).
        andExpect(status().isOk()).andReturn();

    List<GuestDTO> guestDto = mapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);

    Assert.assertEquals(4, guestDto.size());
  }

  @Test
  public void findByName() throws Exception {
    mockMvc.perform(post("/guests").header("user","admin").
        content(mapper.writeValueAsString(guestDTO)).
        contentType(MediaType.APPLICATION_JSON)).
        andExpect(status().isCreated()).andReturn();

    MvcResult mvcResult = mockMvc.perform(get("/guests").param("name", name)
        .header("user","admin").
        contentType(MediaType.APPLICATION_JSON)).
        andExpect(status().isOk()).andReturn();

    List<GuestDTO> guestDto = mapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);

    Assert.assertEquals(1, guestDto.size());
  }

}
