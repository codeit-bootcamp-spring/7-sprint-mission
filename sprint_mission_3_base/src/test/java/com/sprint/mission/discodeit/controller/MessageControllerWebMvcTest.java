package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.config.WebMvcTestSupportConfig;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.service.MessageService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
        controllers = MessageController.class,
        excludeAutoConfiguration = {
                DataSourceAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class
        }
)
@Import(WebMvcTestSupportConfig.class)
class MessageControllerWebMvcTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    MessageService messageService;

    @Test
    @DisplayName("POST /api/messages 성공: 메시지 생성(멀티파트) -> 201 + JSON")
    void create_success_multipart() throws Exception {
        UUID messageId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();

        Instant createdAt = Instant.parse("2025-01-01T00:00:00Z");

        MessageDto response = new MessageDto(
                messageId,
                createdAt,
                null,
                "hello",
                channelId,
                null,
                List.of()
        );

        given(messageService.create(any(), any())).willReturn(response);

        MockMultipartFile data = new MockMultipartFile(
                "messageCreateRequest",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                """
                {
                  "channelId": "%s",
                  "authorId": "%s",
                  "content": "hello"
                }
                """.formatted(channelId, authorId).getBytes()
        );

        MockMultipartFile attachment = new MockMultipartFile(
                "attachments",
                "a.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "file".getBytes()
        );

        mockMvc.perform(
                        multipart("/api/messages")
                                .file(data)
                                .file(attachment)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(messageId.toString()))
                .andExpect(jsonPath("$.channelId").value(channelId.toString()))
                .andExpect(jsonPath("$.content").value("hello"));
    }

    @Test
    @DisplayName("POST /api/messages 실패: Validation 실패 -> 400 + fieldErrors")
    void create_validation_fail() throws Exception {
        UUID channelId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();

        MockMultipartFile data = new MockMultipartFile(
                "messageCreateRequest",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                """
                {
                  "channelId": "%s",
                  "authorId": "%s",
                  "content": ""
                }
                """.formatted(channelId, authorId).getBytes()
        );

        mockMvc.perform(multipart("/api/messages").file(data))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.fieldErrors").exists());
    }

    @Test
    @DisplayName("DELETE /api/messages/{id} 실패: MESSAGE_NOT_FOUND -> 404 + ErrorResponse")
    void delete_not_found() throws Exception {
        UUID messageId = UUID.randomUUID();

        willThrow(new DiscodeitException(ErrorCode.MESSAGE_NOT_FOUND))
                .given(messageService)
                .delete(eq(messageId));

        mockMvc.perform(delete("/api/messages/{id}", messageId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ErrorCode.MESSAGE_NOT_FOUND.name()));
    }
}
