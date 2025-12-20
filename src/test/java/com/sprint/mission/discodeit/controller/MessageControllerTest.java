package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.messageDto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.messageDto.MessageDto;
import com.sprint.mission.discodeit.dto.messageDto.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.page.PageResponse;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.service.MessageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MessageController.class)
@DisplayName("MessageController Test")
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MessageService messageService;

    @Nested
    @DisplayName("메시지 조회")
    class GetMessage {

        @Test
        @DisplayName("id로 메시지를 조회할 수 있다.")
        void getAllByChannelId() throws Exception {
            // given
            UUID channelId = UUID.randomUUID();

            List<MessageDto> messages = List.of(
                    MessageDto.builder().content("1").build(),
                    MessageDto.builder().content("2").build()
            );
            PageResponse<MessageDto> pages = new PageResponse<>(messages, null, 10, true, null);

            when(messageService.findAllByChannelId(eq(channelId), any(), any()))
                    .thenReturn(pages);
            // when & then
            mockMvc.perform(get("/api/messages")
                            .param("channelId", channelId.toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].content").value("1"))
                    .andExpect(jsonPath("$.content[1].content").value("2"));
        }

        @Test
        @DisplayName("존재하지 않는 채널을 조회하면 404를 반환한다.")
        void getAllByChannelId_NotFound() throws Exception {
            // given
            UUID channelId = UUID.randomUUID();

            when(messageService.findAllByChannelId(eq(channelId), any(), any()))
                    .thenThrow(new ChannelNotFoundException(channelId));

            // when & then
            mockMvc.perform(get("/api/messages")
                            .param("channelId", channelId.toString()))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("메시지 생성")
    class CreateMessage {

        @Test
        @DisplayName("메시지를 생성할 수 있다.")
        void createMessage_Success() throws Exception {
            // given
            MessageCreateRequest request = MessageCreateRequest.builder()
                    .build();

            MessageDto response = MessageDto.builder()
                    .build();

            when(messageService.createMessage(any(MessageCreateRequest.class), isNull())).thenReturn(response);

            String json = objectMapper.writeValueAsString(request);
            MockMultipartFile multipartFile
                    = new MockMultipartFile("messageCreateRequest", "",
                    MediaType.APPLICATION_JSON_VALUE, json.getBytes());

            // when & then
            mockMvc.perform(multipart("/api/messages")
                    .file(multipartFile))
                    .andExpect(status().isCreated());
        }
        
        @Test
        @DisplayName("존재하지 않는 채널에 메세지 생성 시 404를 반환한다.")
        void createMessage_ChannelNotFound() throws Exception {
            // given
            UUID channelId = UUID.randomUUID();

            MessageCreateRequest request = MessageCreateRequest.builder().build();

            when(messageService.createMessage(any(MessageCreateRequest.class), isNull()))
                    .thenThrow(new ChannelNotFoundException(channelId));

            String json = objectMapper.writeValueAsString(request);
            MockMultipartFile multipartFile
                    = new MockMultipartFile("messageCreateRequest", "",
                    MediaType.APPLICATION_JSON_VALUE, json.getBytes());
            // when & then
            mockMvc.perform(multipart("/api/messages")
                    .file(multipartFile))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("C001"));
        }
    }

    @Nested
    @DisplayName("메시지 수정")
    class UpdateChannel {

        @Test
        @DisplayName("메시지를 수정할 수 있다.")
        void updateMessage_Success() throws Exception {
            // given
            UUID messageId = UUID.randomUUID();
            MessageUpdateRequest request = MessageUpdateRequest.builder().build();
            MessageDto response = MessageDto.builder().build();

            when(messageService.updateMessage(eq(messageId), any(MessageUpdateRequest.class)))
                    .thenReturn(response);
            String json = objectMapper.writeValueAsString(request);
            // when & then
            mockMvc.perform(patch("/api/messages/" + messageId)
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("존재하지 않는 id로 메시지 수정 시 404를 반환한다.")
        void updateMessage_NotFound() throws Exception {
            // given
            UUID messageId = UUID.randomUUID();
            MessageUpdateRequest request = MessageUpdateRequest.builder().build();

            when(messageService.updateMessage(eq(messageId), any(MessageUpdateRequest.class)))
                    .thenThrow(new MessageNotFoundException(messageId));
            String json = objectMapper.writeValueAsString(request);

            // when & then
            mockMvc.perform(patch("/api/messages/" + messageId)
                    .content(json)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("메시지 삭제")
    class DeleteMessage {

        @Test
        @DisplayName("메시지를 삭제할 수 있다.")
        void deleteMessage_Success() throws Exception {
            // given
            UUID messageId = UUID.randomUUID();

            // when & then
            mockMvc.perform(delete("/api/messages/" + messageId))
                    .andExpect(status().isNoContent());

            verify(messageService).deleteMessage(messageId);
        }

        @Test
        @DisplayName("존재하지 않는 메시지를 삭제 시 404를 반환한다.")
        void deleteMessage_NotFound() throws Exception {
            // given
            UUID messageId = UUID.randomUUID();

            doThrow(new MessageNotFoundException(messageId))
                    .when(messageService).deleteMessage(messageId);
            // when & then
            mockMvc.perform(delete("/api/messages/" + messageId))
                    .andExpect(status().isNotFound());
        }
    }
}