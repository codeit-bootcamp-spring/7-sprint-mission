package com.sprint.mission.discodeit.integration;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.config.TestJpaAuditingConfig;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class MessageApiIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/messages 성공 + GET /api/messages 조회 -> 200")
    void createAndListMessages_success() throws Exception {
        UUID userId = createUser("author1", "a1@e.com");
        UUID channelId = createChannel("channel-msg");

        MockMultipartFile requestPart = new MockMultipartFile(
                "messageCreateRequest",
                "",
                "application/json",
                ("""
                {
                  "content": "hello",
                  "channelId": "%s",
                  "authorId": "%s"
                }
                """.formatted(channelId, userId)).getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(multipart("/api/messages").file(requestPart))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("hello")));

        mockMvc.perform(get("/api/messages").param("channelId", channelId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("hello")));
    }

    @Test
    @DisplayName("POST /api/messages 실패(Validation) -> 400")
    void createMessage_validationFail() throws Exception {
        MockMultipartFile requestPart = new MockMultipartFile(
                "messageCreateRequest",
                "",
                "application/json",
                """
                {
                  "content": "",
                  "channelId": null,
                  "authorId": null
                }
                """.getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(multipart("/api/messages").file(requestPart))
                .andExpect(status().isBadRequest());
    }

    private UUID createUser(String username, String email) throws Exception {
        MockMultipartFile requestPart = new MockMultipartFile(
                "userCreateRequest",
                "",
                "application/json",
                ("""
                {
                  "username": "%s",
                  "email": "%s",
                  "password": "password1234"
                }
                """.formatted(username, email)).getBytes(StandardCharsets.UTF_8)
        );

        MvcResult result = mockMvc.perform(multipart("/api/users").file(requestPart))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
        return UUID.fromString(json.get("id").asText());
    }

    private UUID createChannel(String name) throws Exception {
        MvcResult result = mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/channels")
                                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                  "type": "PUBLIC",
                                  "name": "%s",
                                  "description": "desc"
                                }
                                """.formatted(name))
                )
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
        return UUID.fromString(json.get("id").asText());
    }
}
