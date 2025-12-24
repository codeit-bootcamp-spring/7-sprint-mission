package com.sprint.mission.discodeit.controller;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.CreatePublicChannelDto;
import com.sprint.mission.discodeit.dto.response.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.enum_.ChannelType;
import com.sprint.mission.discodeit.exception.GlobalExceptionHandler;
import com.sprint.mission.discodeit.exception.channel.DuplicateChannelException;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChannelController.class)
@Import({GlobalExceptionHandler.class})
@DisplayName("채널 Controller 테스트")
class ChannelControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private ChannelService channelService;

  private UUID channelId;

  @BeforeEach
  void setUp() {
    channelId = UUID.randomUUID();
  }

  @Nested
  @DisplayName("공개 채널 생성")
  class CreatePublicChannel {

    @Test
    @DisplayName("공개 채널 생성 요청 성공")
    void createPublicChannel_Success() throws Exception {
      // given
      User user = new User("진우", "a@a.com", "1234", null);

      UserResponseDto userResponse = new UserResponseDto(
          user.getId(), user.getUsername(), user.getEmail(), null, true);

      CreatePublicChannelDto request = new CreatePublicChannelDto(
          "공개 채널",
          "채널 설명"
      );

      ChannelResponseDto response = new ChannelResponseDto(
          channelId,
          ChannelType.PUBLIC,
          request.name(),
          request.description(),
          List.of(userResponse),
          Instant.now()
      );

      when(channelService.createPublicChannel(any()))
          .thenReturn(response);

      // when & then
      mockMvc.perform(post("/api/channels/public")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andDo(print())
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.name").value("공개 채널"))
          .andExpect(jsonPath("$.description").value("채널 설명"))
          .andExpect(jsonPath("$.type").value("PUBLIC"));

      verify(channelService).createPublicChannel(any());
    }

    @Test
    @DisplayName("중복된 채널 생성 요청 실패")
    void createPublicChannel_Duplicate() throws Exception {
      // given
      CreatePublicChannelDto request = new CreatePublicChannelDto(
          "공개 채널",
          "채널 설명"
      );

      when(channelService.createPublicChannel(any()))
          .thenThrow(new DuplicateChannelException(request.name()));

      // when & then
      mockMvc.perform(post("/api/channels/public")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andDo(print())
          .andExpect(status().is4xxClientError())
          .andExpect(content().string(containsString("이미 존재하는 채널입니다.")));

      verify(channelService).createPublicChannel(any());

    }


  }

}