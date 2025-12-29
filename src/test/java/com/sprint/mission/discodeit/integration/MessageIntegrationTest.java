package com.sprint.mission.discodeit.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class MessageIntegrationTest extends IntegrationBaseTest {
    @Test
    @DisplayName("메세지 생성 성공")
    void message_create_success() throws Exception {
        UUID authorId = createUser("user", "password1234", "user@naver.com");
        UUID channelId = createPublicChannel("channelName", "description", 0, "PUBLIC");

        UUID messageId = createMessage(channelId, authorId, "hi");

        mockMvc.perform(
                get("/api/messages")
                        .param("channelId", channelId.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(messageId.toString()))
                .andExpect(jsonPath("$.content[0].content").value("hi"))
                .andExpect(jsonPath("$.hasNext").exists());
    }

    @Test
    @DisplayName("메세지 생성 실패")
    void message_create_fail() throws Exception {
        UUID channelId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();

        String badJson = """
                {
                "content": "",
                "authorId": "%s",
                "channelId": "%s"
                }
                """.formatted(authorId, channelId);

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "messageCreateRequest",
                "messageCreateRequest",
                MediaType.APPLICATION_JSON_VALUE,
                badJson.getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(
                multipart("/api/messages")
                        .file(mockMultipartFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.details.fieldErrors").exists())
                .andExpect(jsonPath("$.details.fieldErrors.content").exists());
    }

    @Test
    @DisplayName("메세지 수정 성공")
    void message_update_success() throws Exception {
        UUID userId = createUser("user1", "password1234", "user1@naver.com");
        UUID channelId = createPublicChannel("channel1", "description", 0, "PUBLIC");
        UUID messageId = createMessage(channelId, userId, "hi");

        String updateJson = """
                {
                "newContent": "newContent"
                }
                """;

        mockMvc.perform(
                patch("/api/messages/{messageId}",messageId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(messageId.toString()))
                .andExpect(jsonPath("$.content").value("newContent"));
    }

    @Test
    @DisplayName("메세지 수정 실패")
    void message_update_fail() throws Exception {
        String updateJson = """
                {
                "newContent": "newContent"
                }
                """;

        mockMvc.perform(
                patch("/api/messages/{messageId}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson)
        )
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("메세지 삭제 성공")
    void message_delete_success() throws Exception {
        UUID userId = createUser("user1", "password1234", "user1@naver.com");
        UUID channelId = createPublicChannel("channel1", "description", 0, "PUBLIC");
        UUID messageId = createMessage(channelId, userId, "hi");

        mockMvc.perform(delete("/api/messages/{messageId}", messageId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/messages/{messageId}", messageId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("메세지 삭제 실패")
    void message_delete_fail() throws Exception {
        mockMvc.perform(delete("/api/messages/{messageId}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}
