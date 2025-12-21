package com.sprint.mission.discodeit.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.response.UserResponseDto;
import com.sprint.mission.discodeit.exception.GlobalExceptionHandler;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.basic.BasicUserStatusService;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = UserController.class)
@Import({GlobalExceptionHandler.class})
@DisplayName("유저 Controller 테스트")
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private UserService userService;

  @MockitoBean
  private UserStatusService userStatusService;

  private UUID userId1;
  private UUID userId2;

  @BeforeEach
  void setUp() {
    userId1 = UUID.randomUUID();
    userId2 = UUID.randomUUID();
  }

  @Nested
  @DisplayName("유저 조회 요청")
  class findUsers {

    @Test
    @DisplayName("모든 유저를 조회할 수 있다")
    void findAllUser_Success() throws Exception {
      // given
      List<UserResponseDto> users = List.of(
          new UserResponseDto(userId1, "진우", "a@a.com", null, true),
          new UserResponseDto(userId2, "아토", "b@b.com", null, false)
      );

      // when
      when(userService.findAll())
          .thenReturn(users);

      // then
      mockMvc.perform(get("/api/users"))
          .andDo(print())
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(2)))
          .andExpect(jsonPath("$[0].username").value("진우"))
          .andExpect(jsonPath("$[1].username").value("아토"));

      verify(userService).findAll();
    }
  }

  @Nested
  @DisplayName("유저 삭제 요청")
  class DeleteUser {

    @Test
    @DisplayName("유저를 삭제할 수 있다")
    void deleteUser_Success() throws Exception {
      // given
      UUID deleteUserId = userId1;

      doNothing().when(userService).deleteUser(deleteUserId);

      // when & then
      mockMvc.perform(delete("/api/users/{userId}", deleteUserId))
          .andDo(print())
          .andExpect(status().isNoContent());

      verify(userService).deleteUser(deleteUserId);
    }

    @Test
    @DisplayName("존재하지 않은 유저 삭제 실패")
    void deleteUser_NotFound() throws Exception {
      // given
      UUID notFoundUserId = userId1;

      doThrow(new UserNotFoundException(notFoundUserId))
          .when(userService).deleteUser(notFoundUserId);

      // when & then
      mockMvc.perform(delete("/api/users/{userId}", notFoundUserId))
          .andDo(print())
          .andExpect(status().isNotFound())
          .andExpect(content().string(containsString("유저를 찾을 수 없습니다.")));

      verify(userService).deleteUser(notFoundUserId);

    }
  }


}