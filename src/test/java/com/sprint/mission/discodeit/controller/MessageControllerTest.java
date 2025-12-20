package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.service.dto.response.MessageDto;
import com.sprint.mission.discodeit.service.dto.response.PageResponse;
import com.sprint.mission.discodeit.service.dto.response.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@WebMvcTest(MessageController.class)
@DisplayName("messageController 테스트")
public class MessageControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private MessageService messageService;

    @Nested
    @DisplayName("메세지 생성")
    class CreateMessage {
        @Test
        @DisplayName("메세지 생성 성공")
        void createMessage_success() throws Exception {
            //given
            UUID userId = UUID.randomUUID();
            UUID channelId = UUID.randomUUID();
            MessageCreateRequest messageCreateRequest = new MessageCreateRequest("content", userId, channelId);

            MockMultipartFile file = new MockMultipartFile(
                    "attachments",
                    "file1.txt",
                    MediaType.IMAGE_PNG_VALUE,
                    "fake file".getBytes()
            );
            MockMultipartFile file2 = new MockMultipartFile(
                    "attachments",
                    "file2.txt",
                    MediaType.IMAGE_PNG_VALUE,
                    "fake file".getBytes()
            );

            MessageDto messageDto = new MessageDto();
            messageDto.setContent("content");
            UserDto userDto = new UserDto();
            userDto.setId(userId);
            messageDto.setAuthor(userDto);
            messageDto.setChannelId(channelId);

            given(messageService.sendMessage(any(), anyList()))
                    .willReturn(messageDto);

            MockMultipartFile requestPart = new MockMultipartFile(
                    "messageCreateRequest",
                    "",
                    MediaType.APPLICATION_JSON_VALUE,
                    objectMapper.writeValueAsBytes(messageCreateRequest)
            );


            //when, then
            mockMvc.perform(multipart("/api/messages")
                            .file(requestPart)
                            .file(file)
                            .file(file2)
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.author.id").value(userId.toString()))
                    .andExpect(jsonPath("$.channelId").value(channelId.toString()));

            then(messageService).should().sendMessage(any(), anyList());
        }

        @Test
        @DisplayName("메세지 생성 실패 - messageCreateRequest 누락")
        void createMessage_fail_noMessageCreateRequest() throws Exception {
            // when, then
            mockMvc.perform(multipart("/api/messages")
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            then(messageService).shouldHaveNoInteractions();
        }
    }

    @Nested
    @DisplayName("메세지 수정")
    class UpdateMessage {
        @Test
        @DisplayName("메세지 수정 성공")
        void updateMessage_success() throws Exception {
            // given
            UUID messageId = UUID.randomUUID();

            MessageDto messageDto = new MessageDto();
            messageDto.setContent("updated content");

            given(messageService.updateMessage(eq(messageId), any(MessageUpdateRequest.class)))
                    .willReturn(messageDto);

            // when, then
            mockMvc.perform(
                            multipart("/api/messages/{messageId}", messageId)
                                    .with(requestBuilder -> {
                                        requestBuilder.setMethod("PATCH");
                                        return requestBuilder;
                                    })
                                    .param("newContent", "updated content")
                                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    )
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.content").value("updated content"));

            then(messageService).should()
                    .updateMessage(eq(messageId), any(MessageUpdateRequest.class));
        }

        @Test
        @DisplayName("메세지 수정 실패 - 존재하지 않는 messageId")
        void updateMessage_fail() throws Exception {
            // given
            UUID messageId = UUID.randomUUID();

            given(messageService.updateMessage(eq(messageId), any(MessageUpdateRequest.class)))
                    .willThrow(new MessageNotFoundException(ErrorCode.MESSAGE_NOT_FOUND, new HashMap<>()));

            // when, then
            mockMvc.perform(
                            multipart("/api/messages/{messageId}", messageId)
                                    .with(request -> {
                                        request.setMethod("PATCH");
                                        return request;
                                    })
                                    .param("newContent", "updated content")
                                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            then(messageService).should()
                    .updateMessage(eq(messageId), any(MessageUpdateRequest.class));
        }
    }

    @Nested
    @DisplayName("메세지 삭제")
    class DeleteMessage {
        @Test
        @DisplayName("메세지 삭제 성공")
        void deleteMessage_success() throws Exception {
            // given
            UUID messageId = UUID.randomUUID();
            willDoNothing()
                    .given(messageService)
                    .deleteMessage(messageId);

            // when, then
            mockMvc.perform(delete("/api/messages/{messageId}", messageId))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            then(messageService).should()
                    .deleteMessage(messageId);
        }

        @Test
        @DisplayName("메세지 삭제 실패")
        void deleteMessage_fail() throws Exception {
            // given

            // when, then
            mockMvc.perform(delete("/api/messages/{messageId}", "no-uuid"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            then(messageService).should(never())
                    .deleteMessage(any());
        }
    }

    @Nested
    @DisplayName("메세지 조회")
    class GetMessage {
        @Test
        @DisplayName("채널별 메세지 조회 성공")
        void getAllMessageByChannelId_success() throws Exception {
            // given
            UUID channelId = UUID.randomUUID();
            Instant cursor = Instant.parse("2025-01-01T00:00:00Z");
            int size = 50;
            String sort = "createdAt,desc";

            MessageDto messageDto1 = new MessageDto();
            messageDto1.setContent("message1");

            MessageDto messageDto2 = new MessageDto();
            messageDto2.setContent("message2");

            PageResponse<MessageDto> pageResponse = new PageResponse<>(
                    List.of(messageDto1, messageDto2),
                    messageDto2,
                    2,
                    false,
                    2L);

            given(messageService.getAllByChannelId(
                    eq(channelId),
                    eq(cursor),
                    eq(size),
                    eq(sort)
            )).willReturn(pageResponse);

            // when, then
            mockMvc.perform(get("/api/messages")
                            .param("channelId", channelId.toString())
                            .param("cursor", cursor.toString())
                            .param("size", String.valueOf(size))
                            .param("sort", sort)
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    .andExpect(jsonPath("$.content[0].content").value("message1"))
                    .andExpect(jsonPath("$.content[1].content").value("message2"))
                    .andExpect(jsonPath("$.size").value(2))
                    .andExpect(jsonPath("$.hasNext").value(false));

            then(messageService).should()
                    .getAllByChannelId(eq(channelId), eq(cursor), eq(size), eq(sort));
        }

        @Test
        @DisplayName("채널별 메세지 조회 실패 - channelId 누락")
        void getAllMessageByChannelId_fail_missingChannelId() throws Exception {
            // when, then
            mockMvc.perform(get("/api/messages")
                            .param("size", "50")
                            .param("sort", "DESC")
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            then(messageService).shouldHaveNoInteractions();
        }
    }
}
