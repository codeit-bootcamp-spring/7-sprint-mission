package com.sprint.mission.discodeit.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.channel.request.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.dto.channel.request.CreatePublicChannelDto;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.enums.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Channel 통합 테스트")
public class ChannelIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private UserRepository userRepository;

    @Nested
    @DisplayName("채널 생성 테스트")
    class ChannelCreate {
        private CreatePublicChannelDto createPublicChannelDto;
        private CreatePrivateChannelDto createPrivateChannelDto;

        @BeforeEach
        void setUp() {
            createPublicChannelDto = new CreatePublicChannelDto(
                    "test room",
                    "test description"
            );
        }

        @Test
        @DisplayName("[정상 케이스] - Public 채널 생성")
        void createChannel_public_success() throws Exception {
            // given

            // when
            MvcResult mvcResult = mockMvc.perform(post("/api/channels/public")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createPublicChannelDto)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.type").value("PUBLIC"))
                    .andReturn();

            String responseBody = mvcResult.getResponse().getContentAsString();
            ChannelResponseDto channelResponseDto = objectMapper.readValue(responseBody, ChannelResponseDto.class);

            // then
            Optional<Channel> channel = channelRepository.findById(channelResponseDto.id());
            assertThat(channel).isPresent();
            assertThat(channel.get().getName()).isEqualTo("test room");
        }

        @Test
        @DisplayName("[정상 케이스] - Private 채널 생성")
        void createChannel_private_success() throws Exception {
            // given
            User user = new User("test", "test111@codeit.com", "test_456", null);
            userRepository.save(user);
            UUID userId = user.getId();

            createPrivateChannelDto = new CreatePrivateChannelDto(
                    List.of(userId)
            );

            // when
            MvcResult mvcResult = mockMvc.perform(post("/api/channels/private")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createPrivateChannelDto)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.type").value("PRIVATE"))
                    .andReturn();

            // then
            String responseBody = mvcResult.getResponse().getContentAsString();
            ChannelResponseDto channelResponseDto = objectMapper.readValue(responseBody, ChannelResponseDto.class);

            // then
            Optional<Channel> channel = channelRepository.findById(channelResponseDto.id());
            assertThat(channel).isPresent();
            assertThat(channel.get().getName()).isNull();
        }

        @Test
        @DisplayName("[예외 케이스] - Private 채널 생성 실패 - 존재하지 않는 유저")
        void createChannel_private_fail() throws Exception {
            // given
            User user = new User("test", "test111@codeit.com", "test_456", null);
            userRepository.save(user);
            UUID userId = UUID.randomUUID();

            createPrivateChannelDto = new CreatePrivateChannelDto(
                    List.of(userId)
            );

            // when
            mockMvc.perform(post("/api/channels/private")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createPrivateChannelDto)))
                    .andDo(print())
                    .andExpect(status().isNotFound());


            // then
            List<Channel> channelList = channelRepository.findAll();
            assertThat(channelList).hasSize(0);
        }
    }

    @Nested
    @DisplayName("채널 삭제 테스트")
    class ChannelDelete {
        @Test
        @DisplayName("[정상 케이스] - 채널 삭제 테스트")
        void deleteChannel_success() throws Exception {
            // given
            Channel channel = Channel.builder()
                    .description("channel description")
                    .name("channel name")
                    .type(ChannelType.PUBLIC)
                    .build();
            channelRepository.save(channel);
            UUID channelId = channel.getId();

            // when
            mockMvc.perform(delete("/api/channels/{channelId}", channelId))
                    .andExpect(status().isNoContent());

            // then
            List<Channel> channelList = channelRepository.findAll();
            assertThat(channelList).hasSize(0);

        }

        @Test
        @DisplayName("[예외 케이스] - 채널 삭제 실패 - 존재하지 않는 채널")
        void deleteChannel_notFoundChannel_fail() throws Exception {
            // given
            Channel channel = Channel.builder()
                    .description("channel description")
                    .name("channel name")
                    .type(ChannelType.PUBLIC)
                    .build();
            channelRepository.save(channel);
            UUID channelId = UUID.randomUUID();

            // when
            mockMvc.perform(delete("/api/channels/{channelId}", channelId))
                    .andExpect(status().isNotFound());
            // then
            List<Channel> channelList = channelRepository.findAll();
            assertThat(channelList).hasSize(1);

        }
    }
}
