package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.service.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.service.dto.response.ChannelDto;
import com.sprint.mission.discodeit.service.dto.response.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(ChannelController.class)
@DisplayName("channelController 테스트")
public class ChannelControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    ChannelService channelService;

    @Nested
    @DisplayName("채널 생성")
    class CreateChannel {
        @Test
        @DisplayName("public 채널 생성 성공")
        void createPublicChannel_success() throws Exception {
            // given
            PublicChannelCreateRequest request = new PublicChannelCreateRequest("test", "description");
            UUID channelId = UUID.randomUUID();
            ChannelDto response = new ChannelDto();
            response.setId(channelId);
            response.setName("test");
            response.setDescription("description");
            response.setType(ChannelType.PUBLIC);

            given(channelService.createPublicChannel(request))
                    .willReturn(response);

            // when & then
            mockMvc.perform(post("/api/channels/public")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(channelId.toString()))
                    .andExpect(jsonPath("$.name").value("test"))
                    .andExpect(jsonPath("$.description").value("description"))
                    .andExpect(jsonPath("$.type").value("PUBLIC"));

            then(channelService).should()
                    .createPublicChannel(request);
        }

        @Test
        @DisplayName("public 채널 생성 실패 -  이름 없음")
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

            then(channelService).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("private 채널 생성 성공")
        void createPrivateChannel_success() throws Exception {
            // given
            UUID userId = UUID.randomUUID();
            List<UUID> participants = List.of(userId);
            PrivateChannelCreateRequest request =
                    new PrivateChannelCreateRequest(participants);

            UUID channelId = UUID.randomUUID();
            ChannelDto response = new ChannelDto();
            response.setId(channelId);
            response.setName("dm");
            UserDto userDto = new UserDto();
            userDto.setId(userId);
            response.setParticipants(List.of(userDto));
            response.setType(ChannelType.PRIVATE);

            given(channelService.createPrivateChannel(request))
                    .willReturn(response);

            // when & then
            mockMvc.perform(post("/api/channels/private")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(channelId.toString()))
                    .andExpect(jsonPath("$.name").value("dm"))
                    .andExpect(jsonPath("$.participants", hasSize(1)))
                    .andExpect(jsonPath("$.participants[0].id").value(userId.toString()))
                    .andExpect(jsonPath("$.type").value("PRIVATE"));

            then(channelService).should()
                    .createPrivateChannel(request);
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

            then(channelService).shouldHaveNoInteractions();
        }
    }

    @Nested
    @DisplayName("채널 조회")
    class GetChannel {
        @Test
        @DisplayName("유저별 채널 조회")
        void getAllChannelByUserId_success() throws Exception {
            //given
            UUID userId = UUID.randomUUID();
            ChannelDto channelDto1 = new ChannelDto();
            ChannelDto channelDto2 = new ChannelDto();
            channelDto1.setName("test1");
            channelDto2.setName("test2");
            given(channelService.getAllByUser(userId))
                    .willReturn(List.of(channelDto1, channelDto2));

            //when, then
            mockMvc.perform(get("/api/channels")
                            .param("userId", userId.toString())
                    ).andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].name").value("test1"))
                    .andExpect(jsonPath("$[1].name").value("test2"));

            then(channelService).should().getAllByUser(any());
        }

        @Test
        @DisplayName("유저별 채널 조회 실패 - userId 없음")
        void getAllChannelByUserId_fail() throws Exception {
            // when & then
            mockMvc.perform(get("/api/channels"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            then(channelService).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("유저별 채널 조회 - uuid 형식 x")
        void getAllChannelByUserId_fail_invalidUserId() throws Exception {
            // when & then
            mockMvc.perform(get("/api/channels")
                            .param("userId", "no-uuid"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            then(channelService).shouldHaveNoInteractions();
        }
    }

    @Nested
    @DisplayName("채널 삭제")
    class DeleteChannel {
        @Test
        @DisplayName("채널 삭제 성공")
        void removeChannel_success() throws Exception {
            // given
            UUID channelId = UUID.randomUUID();
            willDoNothing().given(channelService).deleteChannel(channelId);

            // when, then
            mockMvc.perform(delete("/api/channels/{channelId}", channelId))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            then(channelService).should().deleteChannel(channelId);
        }

        @Test
        @DisplayName("채널 삭제 실패 - uuid 형식 x")
        void removeChannel_fail() throws Exception {
            // when, then
            mockMvc.perform(delete("/api/channels/{channelId}", "no-uuid"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            then(channelService).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("채널 삭제 실패 - 존재하지 않는 채널")
        void removeChannel_fail2() throws Exception {
            // given
            UUID channelId = UUID.randomUUID();
            willThrow(new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND, new HashMap<>()))
                    .given(channelService).deleteChannel(channelId);

            // when, then
            mockMvc.perform(delete("/api/channels/{channelId}", channelId))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            then(channelService).should().deleteChannel(channelId);
        }
    }

    @Nested
    @DisplayName("채널 수정")
    class UpdateChannel {
        @Test
        @DisplayName("채널 수정 성공")
        void updateChannel_success() throws Exception {
            //given
            UUID channelId = UUID.randomUUID();
            ChannelUpdateRequest request = new ChannelUpdateRequest("newName", "newDescription");

            ChannelDto channelDto = new ChannelDto();
            channelDto.setName("newName");
            channelDto.setDescription("newDescription");
            given(channelService.updateChannel(channelId, request))
                    .willReturn(channelDto);

            //when, then
            mockMvc.perform(patch("/api/channels/{channelId}", channelId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("newName"))
                    .andExpect(jsonPath("$.description").value("newDescription"));

            then(channelService).should().updateChannel(channelId, request);
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

            then(channelService).shouldHaveNoInteractions();
        }
    }
}
