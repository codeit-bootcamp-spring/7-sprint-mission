package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.response.MessageResponseDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.global.exception.ErrorResponseMapperImpl;
import com.sprint.mission.discodeit.global.exception.GlobalExceptionHandler;
import com.sprint.mission.discodeit.global.exception.discodietException.message.MessageNotFoundException;
import com.sprint.mission.discodeit.global.exception.discodietException.user.UserNotFoundException;
import com.sprint.mission.discodeit.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MessageController.class)
@Import({GlobalExceptionHandler.class, ErrorResponseMapperImpl.class})
@DisplayName("메세지 Controller 테스트")
class MessageControllerTest {
    @Autowired
    private MockMvc mockMvc; // HTTP 요청 시뮬레이션

    @Autowired
    private ObjectMapper objectMapper; // JSON 변환용

    @MockitoBean
    private MessageService messageService;

    @Nested
    @DisplayName("메세지 생성 테스트")
    class MessageCreate {
        private UUID userId;
        private UUID channelId;
        private CreateMessageDto createMessageDto;
        private MessageResponseDto messageResponseDto;
        private UUID messageId;
        private UserResponseDto author;
        private UUID binaryContentId;
        private BinaryContentResponseDto attachment;

        @BeforeEach
        void setUp() {
            userId = UUID.randomUUID();
            channelId = UUID.randomUUID();
            createMessageDto = new CreateMessageDto("content", userId, channelId);

            messageId = UUID.randomUUID();
            author = new UserResponseDto(userId, "test", "test@codeit.com", null, true);
            binaryContentId = UUID.randomUUID();
            attachment = new BinaryContentResponseDto(
                    binaryContentId,
                    Instant.now(),
                    "test.jpg",
                    1L,
                    MediaType.IMAGE_JPEG_VALUE
            );
            messageResponseDto = new MessageResponseDto(
                    messageId,
                    Instant.now(),
                    Instant.now(),
                    "content",
                    channelId,
                    author,
                    List.of(attachment)
            );
        }

        @Test
        @DisplayName("[정상 케이스] - 메세지 생성 성공")
        void createMessage_success() throws Exception {
            // given
            MockMultipartFile messageCreateRequestPart = new MockMultipartFile(
                    "messageCreateRequest",
                    "",
                    MediaType.APPLICATION_JSON_VALUE,
                    objectMapper.writeValueAsBytes(createMessageDto)
            );

            MockMultipartFile attachment = new MockMultipartFile(
                    "attachments",
                    "test.jpg",
                    MediaType.IMAGE_JPEG_VALUE,
                    "attachments".getBytes()
            );

            given(messageService.createMessage(any(CreateMessageDto.class), any(List.class)))
                    .willReturn(messageResponseDto);

            // when
            mockMvc.perform(multipart("/api/messages")
                            .file(messageCreateRequestPart)
                            .file(attachment)
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.content").value("content"))
                    .andExpect(jsonPath("$.attachments[0].fileName").value("test.jpg"));
            // then
            then(messageService).should().createMessage(any(CreateMessageDto.class), any(List.class));
        }

        @Test
        @DisplayName("[예외 케이스] - 메세지 생성 실패 (유저가 없는 경우)")
        void messageCreate_notFoundUser_fail() throws Exception {
            // given
            UUID notFoundUserId = UUID.randomUUID();
            CreateMessageDto notFoundUsercreateMessageDto = new CreateMessageDto("content", notFoundUserId, channelId);

            MockMultipartFile messageCreateRequestPart = new MockMultipartFile(
                    "messageCreateRequest",
                    "",
                    MediaType.APPLICATION_JSON_VALUE,
                    objectMapper.writeValueAsBytes(createMessageDto)
            );

            MockMultipartFile attachment = new MockMultipartFile(
                    "attachments",
                    "test.jpg",
                    MediaType.IMAGE_JPEG_VALUE,
                    "attachments".getBytes()
            );

            doThrow(UserNotFoundException.byId(notFoundUserId)).when(messageService)
                    .createMessage(eq(notFoundUsercreateMessageDto), any(List.class));
            // when
            mockMvc.perform(multipart("/api/messages")
                            .file(messageCreateRequestPart)
                            .file(attachment)
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andDo(print())
                    .andExpect(status().isNotFound());

            // then
            then(messageService).should().createMessage(any(CreateMessageDto.class), any(List.class));
        }
    }

    @Nested
    @DisplayName("메세지 삭제 테스트")
    class MessageDelete {
        private UUID messageId;

        @BeforeEach
        void setUp() {
            messageId = UUID.randomUUID();
        }

        @Test
        @DisplayName("[정상 케이스] - 메세지 삭제")
        void deleteMessage_success() throws Exception {
            // given
            willDoNothing().given(messageService).deleteMessage(any(UUID.class));

            // when
            mockMvc.perform(delete("/api/messages/{messageId}", messageId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            // then
            then(messageService).should().deleteMessage(any(UUID.class));
        }

        @Test
        @DisplayName("[예외 케이스] - 메세지 삭제 (존재하지 않는 메세지)")
        void deleteMessage_notFoundMessageId_fail() throws Exception {
            // given
            doThrow(MessageNotFoundException.byId(messageId))
                    .when(messageService).deleteMessage(any(UUID.class));

            // when
            mockMvc.perform(delete("/api/messages/{messageId}", messageId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound());

            // then
            then(messageService).should().deleteMessage(any(UUID.class));
        }
    }


}