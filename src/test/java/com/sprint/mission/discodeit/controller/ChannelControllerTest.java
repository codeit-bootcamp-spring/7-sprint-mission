//package com.sprint.mission.discodeit.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.sprint.mission.discodeit.dto.channelDto.ChannelDto;
//import com.sprint.mission.discodeit.dto.channelDto.PrivateChannelCreateRequest;
//import com.sprint.mission.discodeit.dto.channelDto.PublicChannelCreateRequest;
//import com.sprint.mission.discodeit.dto.channelDto.PublicChannelUpdateRequest;
//import com.sprint.mission.discodeit.entity.role.ChannelType;
//import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
//import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
//import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
//import com.sprint.mission.discodeit.service.ChannelService;
//import com.sprint.mission.discodeit.service.UserService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//import java.util.UUID;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(ChannelController.class)
//@DisplayName("ChannelController Test")
//class ChannelControllerTest {
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockitoBean
//    private ChannelService channelService;
//    @MockitoBean
//    private UserService userService;
//
//    @Nested
//    @DisplayName("채널 조회")
//    class GetChannel {
//
//        @Test
//        @DisplayName("특정 유저가 볼 수 있는 모든 채널을 조회할 수 있다.")
//        void getAllByUserId_Success() throws Exception {
//            // given
//            UUID userId = UUID.randomUUID();
//
//            ChannelDto c1 = ChannelDto.builder().name("채널1").type(ChannelType.PUBLIC).build();
//            ChannelDto c2 = ChannelDto.builder().name("채널2").type(ChannelType.PUBLIC).build();
//            List<ChannelDto> channels = List.of(c1, c2);
//
//            when(channelService.findAllByUserId(userId)).thenReturn(channels);
//
//            // when & then
//            mockMvc.perform(get("/api/channels?userId=" + userId))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.[0].name").value("채널1"))
//                    .andExpect(jsonPath("$.[1].name").value("채널2"));
//        }
//
//        @Test
//        @DisplayName("존재하지 않는 사용자Id로 조회하면 404를 반환한다.")
//        void getAllByUserId_NotFoundUser() throws Exception {
//            // given
//            UUID userId = UUID.randomUUID();
//            when(channelService.findAllByUserId(userId)).thenThrow(new UserNotFoundException(userId));
//
//            // when & then
//            mockMvc.perform(get("/api/channels").param("userId", userId.toString()))
//                    .andExpect(status().isNotFound())
//                    .andExpect(jsonPath("$.message").value("사용자를 찾을 수 없습니다."))
//                    .andExpect(jsonPath("$.code").value("U001"));
//        }
//    }
//
//    @Nested
//    @DisplayName("채널 생성")
//    class CreateChannel {
//
//        @Test
//        @DisplayName("공개 채널을 생성할 수 있다.")
//        void createPublicChannel_Success() throws Exception {
//            // given
//            PublicChannelCreateRequest request
//                    = new PublicChannelCreateRequest("공개채널", "공지사항");
//
//            ChannelDto result = ChannelDto.builder()
//                    .name("공개채널")
//                    .description("공지사항")
//                    .type(ChannelType.PUBLIC)
//                    .build();
//
//            String json = objectMapper.writeValueAsString(request);
//
//
//            when(channelService.createPublicChannel(any(PublicChannelCreateRequest.class))).thenReturn(result);
//            // when & then
//            mockMvc.perform(post("/api/channels/public")
//                            .content(json)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isCreated());
//        }
//
//        @Test
//        @DisplayName("프라이빗 채널을 생성할 수 있다.")
//        void createPrivateChannel_Success() throws Exception {
//            // given
//            PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(null);
//            ChannelDto result = ChannelDto.builder().type(ChannelType.PRIVATE).build();
//
//            String json = objectMapper.writeValueAsString(request);
//
//            when(channelService.createPrivateChannel(any(PrivateChannelCreateRequest.class))).thenReturn(result);
//            // when & then
//            mockMvc.perform(post("/api/channels/private")
//                            .content(json)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isCreated());
//        }
//    }
//
//    @Nested
//    @DisplayName("채널 정보를 수정할 수 있다.")
//    class UpdateChannel {
//
//        @Test
//        @DisplayName("채널 id로 공개채널 정보를 수정할 수 있다.")
//        void updatePublicChannel_Success() throws Exception {
//            // given
//            UUID channelId = UUID.randomUUID();
//            PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("newName", "newDescription");
//            ChannelDto result = ChannelDto.builder()
//                    .id(channelId)
//                    .name("newName")
//                    .description("newDescription")
//                    .type(ChannelType.PUBLIC)
//                    .build();
//
//            String json = objectMapper.writeValueAsString(request);
//
//            when(channelService.updateChannel(eq(channelId), any(PublicChannelUpdateRequest.class)))
//                    .thenReturn(result);
//
//            // when & then
//            mockMvc.perform(patch("/api/channels/" + channelId)
//                            .content(json)
//                            .contentType(MediaType.APPLICATION_JSON)
//                    )
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.name").value("newName"))
//                    .andExpect(jsonPath("$.description").value("newDescription"));
//        }
//
//        @Test
//        @DisplayName("존재하지 않는 채널 id로 수정 시 404를 반환한다.")
//        void updateChannel_NotFoundChannel() throws Exception {
//            // given
//            UUID channelId = UUID.randomUUID();
//            PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("newName", "NewDescription");
//
//            String json = objectMapper.writeValueAsString(request);
//            when(channelService.updateChannel(eq(channelId), any(PublicChannelUpdateRequest.class)))
//                    .thenThrow(new ChannelNotFoundException(channelId));
//
//            // when & then
//            mockMvc.perform(patch("/api/channels/" + channelId)
//                            .content(json)
//                            .contentType(MediaType.APPLICATION_JSON)
//                    )
//                    .andExpect(status().isNotFound())
//                    .andExpect(jsonPath("$.message").value("채널을 찾을 수 없습니다."))
//                    .andExpect(jsonPath("$.code").value("C001"));
//        }
//
//        @Test
//        @DisplayName("프라이빗 채널은 수정할 수 없다.")
//        void updatePrivateChannel_Failed() throws Exception {
//            // given
//            UUID channelId = UUID.randomUUID();
//            PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("newName", "NewDescription");
//
//            String json = objectMapper.writeValueAsString(request);
//            when(channelService.updateChannel(eq(channelId), any(PublicChannelUpdateRequest.class)))
//                    .thenThrow(new PrivateChannelUpdateException(channelId));
//
//            // when & then
//            mockMvc.perform(patch("/api/channels/" + channelId)
//                            .content(json)
//                            .contentType(MediaType.APPLICATION_JSON)
//                    )
//                    .andExpect(status().isForbidden())
//                    .andExpect(jsonPath("$.message").value("프라이빗 채널은 수정할 수 없습니다."))
//                    .andExpect(jsonPath("$.code").value("C002"));
//        }
//    }
//
//    @Nested
//    @DisplayName("채널 삭제")
//    class DeleteChannel {
//
//        @Test
//        @DisplayName("id로 채널을 삭제할 수 있다.")
//        void deleteChannel_Success() throws Exception {
//            // given
//            UUID channelId = UUID.randomUUID();
//
//            // when & then
//            mockMvc.perform(delete("/api/channels/" + channelId))
//                    .andExpect(status().isNoContent());
//
//            verify(channelService).deleteChannel(channelId);
//        }
//        @Test
//        @DisplayName("존재하지 않는 채널 삭제 시 404를 반환한다.")
//        void deleteChannel_NotFoundChannel() throws Exception {
//            // given
//            UUID channelId = UUID.randomUUID();
//
//            doThrow(new ChannelNotFoundException(channelId))
//                    .when(channelService).deleteChannel(channelId);
//            // when & then
//            mockMvc.perform(delete("/api/channels/" + channelId))
//                    .andExpect(status().isNotFound());
//        }
//    }
//}