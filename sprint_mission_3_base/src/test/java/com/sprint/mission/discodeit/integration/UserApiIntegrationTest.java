package com.sprint.mission.discodeit.integration;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
class UserApiIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/users 성공 -> 201")
    void createUser_success() throws Exception {
        MockMultipartFile requestPart = new MockMultipartFile(
                "userCreateRequest",
                "",
                "application/json",
                """
                {
                  "username": "taehun",
                  "email": "t@e.com",
                  "password": "password1234"
                }
                """.getBytes(StandardCharsets.UTF_8)
        );

        MvcResult result = mockMvc.perform(
                        multipart("/api/users")
                                .file(requestPart)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value("taehun"))
                .andExpect(jsonPath("$.email").value("t@e.com"))
                .andReturn();

        String body = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JsonNode json = objectMapper.readTree(body);
        UUID userId = UUID.fromString(json.get("id").asText());

        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("POST /api/users 실패(Validation) -> 400 + VALIDATION_FAILED")
    void createUser_validationFail() throws Exception {
        MockMultipartFile requestPart = new MockMultipartFile(
                "userCreateRequest",
                "",
                "application/json",
                """
                {
                  "username": "",
                  "email": "not-an-email",
                  "password": ""
                }
                """.getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(multipart("/api/users").file(requestPart))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"))
                .andExpect(jsonPath("$.details.fieldErrors").exists());
    }

    @Test
    @DisplayName("GET /api/users 목록 조회 -> 200")
    void findAllUsers_success() throws Exception {
        MockMultipartFile requestPart = new MockMultipartFile(
                "userCreateRequest",
                "",
                "application/json",
                """
                {
                  "username": "u1",
                  "email": "u1@e.com",
                  "password": "password1234"
                }
                """.getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(multipart("/api/users").file(requestPart))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("u1")));
    }
}
