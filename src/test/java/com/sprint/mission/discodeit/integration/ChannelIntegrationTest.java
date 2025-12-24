package com.sprint.mission.discodeit.integration;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.service.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.PublicChannelCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChannelIntegrationTest extends IntegrationTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ChannelRepository channelRepository;
    @Autowired
    ReadStatusRepository readStatusRepository;

    @Nested
    @DisplayName("채널 생성")
    class CreateChannel {
        @Test
        @DisplayName("public 채널 생성 성공")
        void createPublicChannel_success() throws Exception {
            // given
            PublicChannelCreateRequest request = new PublicChannelCreateRequest("test", "description");


            // when & then
            mockMvc.perform(post("/api/channels/public")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name").value("test"))
                    .andExpect(jsonPath("$.description").value("description"))
                    .andExpect(jsonPath("$.type").value("PUBLIC"));
        }

        @Test
        @DisplayName("public 채널 생성 실패 - 이름 없음")
        void createPublicChannel_fail() throws Exception {
            // given
            PublicChannelCreateRequest request =
                    new PublicChannelCreateRequest(null, "description");

            // when & then
            mockMvc.perform(post("/api/channels/public")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("private 채널 생성 성공")
        void createPrivateChannel_success() throws Exception {
            // given
            User user = new User("test@email.com", "1234", "test");
            User save = userRepository.save(user);
            User user2 = new User("test2@email.com", "1234", "test2");
            User save2 = userRepository.save(user2);
            List<UUID> participants = List.of(save.getId(), save2.getId());
            PrivateChannelCreateRequest request =
                    new PrivateChannelCreateRequest(participants);

            // when & then
            mockMvc.perform(post("/api/channels/private")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name").value("dm"))
                    .andExpect(jsonPath("$.participants", hasSize(2)))
                    .andExpect(jsonPath("$.type").value("PRIVATE"));
        }

        @Test
        @DisplayName("private 채널 생성 실패")
        void createPrivateChannel_fail() throws Exception {
            // given
            PrivateChannelCreateRequest request =
                    new PrivateChannelCreateRequest(null);

            // when & then
            mockMvc.perform(post("/api/channels/private")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("채널 조회")
    class GetChannel {
        @Test
        @DisplayName("유저별 채널 조회")
        void getAllChannelByUserId_success() throws Exception {
            //given
            User user = new User("test@email.com", "1234", "test");
            User save = userRepository.save(user);
            Channel channel = new Channel("test", "description", ChannelType.PUBLIC);
            channelRepository.save(channel);
            ReadStatus readStatus = new ReadStatus(user, channel);
            readStatusRepository.save(readStatus);

            //when, then
            mockMvc.perform(get("/api/channels")
                            .param("userId", user.getId().toString())
                    ).andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].name").value("test"));
        }

        @Test
        @DisplayName("유저별 채널 조회 실패 - userId 없음")
        void getAllChannelByUserId_fail() throws Exception {
            // when & then
            mockMvc.perform(get("/api/channels"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("유저별 채널 조회 - uuid 형식 x")
        void getAllChannelByUserId_fail_invalidUserId() throws Exception {
            // when & then
            mockMvc.perform(get("/api/channels")
                            .param("userId", "no-uuid"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("채널 삭제")
    class DeleteChannel {
        @Test
        @DisplayName("채널 삭제 성공")
        void removeChannel_success() throws Exception {
            // given
            Channel channel = new Channel("test", "description", ChannelType.PUBLIC);
            channelRepository.save(channel);

            // when, then
            mockMvc.perform(delete("/api/channels/{channelId}", channel.getId()))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("채널 삭제 실패 - uuid 형식 x")
        void removeChannel_fail() throws Exception {
            // when, then
            mockMvc.perform(delete("/api/channels/{channelId}", "no-uuid"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("채널 삭제 실패 - 존재하지 않는 채널")
        void removeChannel_fail2() throws Exception {
            // when, then
            mockMvc.perform(delete("/api/channels/{channelId}", UUID.randomUUID()))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("채널 수정")
    class UpdateChannel {
        @Test
        @DisplayName("채널 수정 성공")
        void updateChannel_success() throws Exception {
            //given
            Channel channel = new Channel("test", "description", ChannelType.PUBLIC);
            channelRepository.save(channel);

            ChannelUpdateRequest request = new ChannelUpdateRequest("newName", "newDescription");

            //when, then
            mockMvc.perform(patch("/api/channels/{channelId}", channel.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("newName"))
                    .andExpect(jsonPath("$.description").value("newDescription"));
        }

        @Test
        @DisplayName("채널 수정 실패 - body 누락")
        void updateChannel_fail() throws Exception {
            //given
            UUID channelId = UUID.randomUUID();

            // when, then
            mockMvc.perform(patch("/api/channels/{channelId}", channelId)
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }
}
