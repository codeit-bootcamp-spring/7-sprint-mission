package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.dto.Dto_UserCreate;
import com.sprint.mission.discodeit.entity.dto.Res_User;
import com.sprint.mission.discodeit.service.basic.UserService;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
public class UserControllerTest {

  @MockBean
  private UserService userService;

  @MockBean
  private Dto_UserCreate dtoUserCreate;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("[⭕️k 케이스] - UserController.create 테스트")
  void createUserTest() throws Exception {

    var resUser = Res_User.builder()
        .id(UUID.randomUUID())
        .username("🦁lion")
        .email("lion@E.m")
        .password("1234")
        .createdAt(Instant.now())
        .updatedAt(Instant.now())
        .profileId(null)
        .build();

    BDDMockito.given(
        userService.create(ArgumentMatchers.any(Dto_UserCreate.class), ArgumentMatchers.isNull()))
    .willReturn(resUser);

    var dto_UserCreate = Dto_UserCreate.builder()
        .username("🦁lion")
        .email("lion@E.m")
        .password("1234")
        .build();

//    mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
//        .contentType(MediaType.APPLICATION_JSON)
//        .accept(MediaType.APPLICATION_JSON)
//        .content(objectMapper.writeValueAsString(dto_UserCreate))
//    )
//        .andDo(print())
//        .andExpect(status().isCreated());
//
//    BDDMockito.then(userService)
//        .should()
//        .create(ArgumentMatchers.any(), ArgumentMatchers.isNull());
  }
}
