package com.sprint.mission.discodeit.Integration;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.CreatePublicChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.enum_.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
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
@DisplayName("Channel 통합 테스트")
public class ChannelIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ChannelRepository channelRepository;

  @Test
  @DisplayName("공개 채널 생성할 수 있다")
  void createPublicChannel_Success() throws Exception {
    // given
    CreatePublicChannelDto request = new CreatePublicChannelDto(
        "공개 채널",
        "공개채널입니다."
    );

    // when
    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("공개 채널"))
        .andExpect(jsonPath("$.description").value("공개채널입니다."));

    // then
    Channel savedChannel = channelRepository.findByNameAndType("공개 채널", ChannelType.PUBLIC)
        .orElseThrow();

    assertThat(savedChannel.getName()).isEqualTo("공개 채널");
    assertThat(savedChannel.getDescription()).isEqualTo("공개채널입니다.");
    assertThat(savedChannel.getCreatedAt()).isNotNull();
  }


  @Test
  @DisplayName("채널 삭제를 성공할 수 있다")
  void deleteChannel_Success() throws Exception {
    // given
    Channel channel = channelRepository.save(
        new Channel(ChannelType.PUBLIC, "공개 채널", "채널 설명입니다.")
    );
    UUID channelId = channel.getId();

    // when
    mockMvc.perform(delete("/api/channels/{channelId}", channelId))
        .andExpect(status().isNoContent());

    // then
    boolean exists = channelRepository.existsById(channelId);
    assertThat(exists).isFalse();
  }
}
