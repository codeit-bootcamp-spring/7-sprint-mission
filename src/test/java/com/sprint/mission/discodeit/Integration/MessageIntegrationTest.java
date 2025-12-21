package com.sprint.mission.discodeit.Integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.CreateUserStatusRequestDto;
import com.sprint.mission.discodeit.dto.update.UpdateMessageDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.enum_.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@DisplayName("Message 통합 테스트")
public class MessageIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ChannelRepository channelRepository;

  @Autowired
  private UserStatusService userStatusService;


  @Test
  @DisplayName("메시지 수정을 성공할 수 있다")
  void updateMessage_Success() throws Exception {
    // given
    User saveduser = userRepository.save(new User("진우", "a@a.com", "1234", null));
    userStatusService.createUserStatus(new CreateUserStatusRequestDto(saveduser.getId()));

    saveduser.assignStatus(new UserStatus(saveduser));

    Channel channel = channelRepository.save(
        new Channel(ChannelType.PUBLIC, "공개 채널", "설명")
    );
    Message message = messageRepository.save(
        new Message("메시지", channel, saveduser, List.of())
    );
    UUID messageId = message.getId();

    UpdateMessageDto request = new UpdateMessageDto("수정된 메시지");

    // when
    mockMvc.perform(patch("/api/messages/{messageId}", messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").value("수정된 메시지"));

    // then
    Message updatedMessage = messageRepository.findById(messageId)
        .orElseThrow();

    assertThat(updatedMessage.getContent()).isEqualTo("수정된 메시지");
  }

  @Test
  @DisplayName("메시지 삭제를 성공할 수 있다")
  void deleteMessage_Success() throws Exception {
    // given
    User user = userRepository.save(new User("진우", "a@a.com", "1234", null));

    Channel channel = channelRepository.save(
        new Channel(ChannelType.PUBLIC, "공개 채널", "설명")
    );
    Message message = messageRepository.save(
        new Message("테스트 메시지", channel, user, null)
    );
    UUID messageId = message.getId();

    // when
    mockMvc.perform(delete("/api/messages/{messageId}", messageId))
        .andExpect(status().isNoContent());

    // then
    boolean exists = messageRepository.existsById(messageId);
    assertThat(exists).isFalse();
  }
}
