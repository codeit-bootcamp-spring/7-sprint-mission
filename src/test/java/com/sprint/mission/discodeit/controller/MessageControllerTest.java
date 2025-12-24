package com.sprint.mission.discodeit.controller;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.response.MessageResponseDto;
import com.sprint.mission.discodeit.dto.response.UserResponseDto;
import com.sprint.mission.discodeit.dto.update.UpdateMessageDto;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@Import({MessageController.class})
@DisplayName("메시지 Controller 테스트")
class MessageControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private MessageService messageService;

  @MockitoBean
  private UserService userService;
  @MockitoBean
  private AuthService authService;
  @MockitoBean
  private BinaryContentService binaryContentService;
  @MockitoBean
  private ChannelService channelService;
  @MockitoBean
  private ReadStatusService readStatusService;
  @MockitoBean
  private UserStatusService userStatusService;
  @MockitoBean
  private BinaryContentStorage binaryContentStorage;

  private UUID messageId;
  private UUID channelId;
  private UUID userId;

  @BeforeEach
  void setUp() {
    messageId = UUID.randomUUID();
    channelId = UUID.randomUUID();
    userId = UUID.randomUUID();
  }

  @Nested
  @DisplayName("메세지 수정 요청")
  class UpdateMessage {

    @Test
    @DisplayName("메세지 수정 요청 성공")
    void updateMessage_Success() throws Exception {
      // given
      UpdateMessageDto updateRequest = new UpdateMessageDto("수정한 내용");

      UserResponseDto user = new UserResponseDto(
          userId, "진우", "a@a.com", null, true);

      MessageResponseDto response = new MessageResponseDto(
          messageId,
          Instant.now(),
          Instant.now(),
          "수정한 내용",
          channelId,
          user,
          null
      );

      when(messageService.updateMessage(eq(messageId), any()))
          .thenReturn(response);

      // when & then
      mockMvc.perform(patch("/api/messages/{messageId}", messageId)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(updateRequest)))
          .andDo(print())
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.content").value("수정한 내용"));

      verify(messageService).updateMessage(eq(messageId), any());
    }
  }

  @Nested
  @DisplayName("메시지 삭제 요청")
  class DeleteMessage {

    @Test
    @DisplayName("메시지 삭제 요청 성공")
    void deleteMessage_Success() throws Exception {
      // given
      doNothing().when(messageService).deleteMessage(messageId);

      // when & then
      mockMvc.perform(delete("/api/messages/{messageId}", messageId))
          .andDo(print())
          .andExpect(status().isNoContent());

      verify(messageService).deleteMessage(messageId);
    }

    @Test
    @DisplayName("존재하지 않은 메시지 삭제 요청 실패")
    void deleteMessage_NotFound() throws Exception {
      // given
      doThrow(new MessageNotFoundException(messageId))
          .when(messageService).deleteMessage(messageId);

      // when & then

      mockMvc.perform(delete("/api/messages/{messageId}", messageId))
          .andDo(print())
          .andExpect(status().isNotFound())
          .andExpect(content().string(containsString("메시지를 찾을 수 없습니다.")));

      verify(messageService).deleteMessage(messageId);

    }
  }

}