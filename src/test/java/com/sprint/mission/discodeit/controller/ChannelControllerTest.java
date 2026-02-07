package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.channel.request.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.dto.channel.request.CreatePublicChannelDto;
import com.sprint.mission.discodeit.dto.channel.request.UpdateChannelDto;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.enums.ChannelType;
import com.sprint.mission.discodeit.entity.enums.Role;
import com.sprint.mission.discodeit.global.exception.ErrorResponseMapperImpl;
import com.sprint.mission.discodeit.global.exception.GlobalExceptionHandler;
import com.sprint.mission.discodeit.global.exception.discodietException.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.global.exception.discodietException.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.service.ChannelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChannelController.class)
@Import({GlobalExceptionHandler.class, ErrorResponseMapperImpl.class})
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("채널 Controller 테스트")
class ChannelControllerTest {
    @Autowired
    private MockMvc mockMvc; // HTTP 요청 시뮬레이션

    @Autowired
    private ObjectMapper objectMapper; // JSON 변환용

    @MockitoBean
    private ChannelService channelService;

    private CreatePublicChannelDto createPublicChannelDto;
    private CreatePrivateChannelDto createPrivateChannelDto;
    private ChannelResponseDto publicChannelResponseDto;
    private ChannelResponseDto privateChannelResponseDto;
    private UserResponseDto userResponseDto1;
    private UserResponseDto userResponseDto2;

    private UUID channelId1;
    private UUID channelId2;
    private UUID userId1;
    private UUID userId2;


    @BeforeEach
    void setup() {
        channelId1 = UUID.randomUUID();
        channelId2 = UUID.randomUUID();
        userId1 = UUID.randomUUID();
        userId2 = UUID.randomUUID();
        userResponseDto1 = new UserResponseDto(
                userId1,
                "test",
                "test@codeit.com",
                null,
                true,
                Role.USER
        );
        userResponseDto2 = new UserResponseDto(
                userId2,
                "test2",
                "test2@codeit.com",
                null,
                true,
                Role.USER
        );

        createPublicChannelDto = new CreatePublicChannelDto("test", "test_desc");
        createPrivateChannelDto = new CreatePrivateChannelDto(List.of(userId1, userId2));
        publicChannelResponseDto = new ChannelResponseDto(
                channelId1,
                ChannelType.PUBLIC,
                "test channel",
                "test description",
                List.of(),
                Instant.now()
        );

        privateChannelResponseDto = new ChannelResponseDto(
                channelId2,
                ChannelType.PRIVATE,
                null,
                null,
                List.of(userResponseDto1, userResponseDto2),
                Instant.now()
        );
    }

    @Nested
    @DisplayName("채널 생성 테스트")
    class ChannelCreate {
        @Test
        @DisplayName("[정상 케이스] - Public 채널 생성 성공")
        void createChannel_public_success() throws Exception {
            // given
            given(channelService.createChannel(createPublicChannelDto))
                    .willReturn(publicChannelResponseDto);

            // when
            mockMvc.perform(post("/api/channels/public")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createPublicChannelDto)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name").value("test channel"));

            then(channelService).should().createChannel(any(CreatePublicChannelDto.class));
        }

        @Test
        @DisplayName("[정상 케이스] - Private 채널 생성 성공")
        void createChannel_private_success() throws Exception {
            // given
            given(channelService.createChannel(createPrivateChannelDto))
                    .willReturn(privateChannelResponseDto);

            // when
            mockMvc.perform(post("/api/channels/private")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createPrivateChannelDto)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.type").value("PRIVATE"))
                    .andExpect(jsonPath("$.name").isEmpty())
                    .andExpect(jsonPath("$.description").isEmpty())
                    .andExpect(jsonPath("$.participants.size()").value(2));

            then(channelService).should().createChannel(any(CreatePrivateChannelDto.class));
        }

        @Test
        @DisplayName("[예외 케이스] - 채널 생성 실패 (request 필드 누락)")
        void createChannel_public_fail() throws Exception {
            createPublicChannelDto = new CreatePublicChannelDto(null, "test_desc");
            // when
            mockMvc.perform(post("/api/channels/public")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createPublicChannelDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());


            then(channelService).should(never()).createChannel(any(CreatePublicChannelDto.class));
        }
    }

    @Nested
    @DisplayName("채널 수정 테스트")
    class ChannelUpdate {
        private UpdateChannelDto updateChannelDto;
        private ChannelResponseDto updatedChannelResponseDto;

        @BeforeEach
        void setup() {
            updateChannelDto = new UpdateChannelDto("new name", "new description");
            updatedChannelResponseDto = new ChannelResponseDto(
                    channelId1,
                    ChannelType.PUBLIC,
                    "new name",
                    "new description",
                    List.of(),
                    Instant.now()
            );
        }

        @Test
        @DisplayName("[정상 케이스] - 채널 수정 성공")
        void updateChannel_success() throws Exception {
            // given
            given(channelService.updateChannel(channelId1, updateChannelDto))
                    .willReturn(updatedChannelResponseDto);

            // when
            mockMvc.perform(patch("/api/channels/{channelId}", channelId1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateChannelDto)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("new name"));

            then(channelService).should().updateChannel(any(UUID.class), any(UpdateChannelDto.class));
        }

        @Test
        @DisplayName("[예외 케이스] - 채널 수정 실패 - 존재하지 않는 채널")
        void updateChannel_fail() throws Exception {
            // given
            UUID notFoundChannelId = UUID.randomUUID();
            doThrow(ChannelNotFoundException.byId(notFoundChannelId))
                    .when(channelService).updateChannel(eq(notFoundChannelId), any(UpdateChannelDto.class));

            // when
            mockMvc.perform(patch("/api/channels/{channelId}", notFoundChannelId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateChannelDto)))
                    .andDo(print())
                    .andExpect(status().isNotFound());

            then(channelService).should().updateChannel(any(UUID.class), any(UpdateChannelDto.class));
        }

        @Test
        @DisplayName("[예외 케이스] - 채널 수정 실패 - PrivateChannel")
        void updateChannel_private_fail() throws Exception {
            // given
            doThrow(PrivateChannelUpdateException.notAllowed(channelId2))
                    .when(channelService).updateChannel(channelId2, updateChannelDto);

            // when
            mockMvc.perform(patch("/api/channels/{channelId}", channelId2)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateChannelDto)))
                    .andDo(print())
                    .andExpect(status().isForbidden());

            then(channelService).should().updateChannel(any(UUID.class), any(UpdateChannelDto.class));
        }
    }
}

