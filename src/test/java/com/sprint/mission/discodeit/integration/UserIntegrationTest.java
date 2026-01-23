package com.sprint.mission.discodeit.integration;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserIntegrationTest extends IntegrationBaseTest{
    @Test
    @DisplayName("사용자 생성 성공")
    void user_create_success() throws Exception {
        UUID user = createUser("user", "password1234", "user@naver.com");
        assertThat(user).isNotNull();
    }

    @Test
    @DisplayName("사용자 생성 실패")
    void user_create_fail() throws Exception {
        String badJson = """
                {
                "username": "",
                "password": "password1234",
                "email": "user@naver.com"
                }
                """;

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "userCreateRequest",
                "userCreateRequest",
                MediaType.APPLICATION_JSON_VALUE,
                badJson.getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(
                multipart("/api/users")
                        .file(mockMultipartFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .characterEncoding(StandardCharsets.UTF_8)
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.details.fieldErrors").exists())
                .andExpect(jsonPath("$.details.fieldErrors.username").exists());
    }
    
    @Test
    @DisplayName("사용자 수정 성공: username/email/password 변경")
    void user_update_success() throws Exception{
        UUID userId = createUser("user", "password1234", "user@naver.com");

        String updateJson = """
                {
                "newUsername": "newUser",
                "newEmail": "newUser@naver.com",
                "newPassword": "newPassword1234"
                }
                """;

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "userUpdateRequest",
                "userUpdateRequest",
                MediaType.APPLICATION_JSON_VALUE,
                updateJson.getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(
                multipartPatch("/api/users/{userId}", userId)
                        .file(mockMultipartFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .characterEncoding(StandardCharsets.UTF_8)
                )
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//        System.out.println("여기가 디버깅");
//        System.out.println(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.username").value("newUser"))
                .andExpect(jsonPath("$.email").value("newUser@naver.com"));

        String content = mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        JsonNode jsonNode = objectMapper.readTree(content);
        JsonNode found = null;
        for (JsonNode node : jsonNode) {
            if (userId.toString().equals(node.get("id").asText())) {
                found = node;
                break;
            }
        }
        assertThat(found).isNotNull();
        assertThat(found.get("username").asText()).isEqualTo("newUser");
        assertThat(found.get("email").asText()).isEqualTo("newUser@naver.com");

    }

    @Test
    @DisplayName("사용자 수정 실패")
    void user_update_fail() throws Exception {
        UUID userId = createUser("user2", "password1234", "user2@naver.com");

        String badJson = """
                {
                "newUsername": "a",
                "newEmail": null,
                "newPassword": null
                }
                """;

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "userUpdateRequest",
                "userUpdateRequest",
                MediaType.APPLICATION_JSON_VALUE,
                badJson.getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(
                multipartPatch("/api/users/{userId}", userId)
                        .file(mockMultipartFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .characterEncoding(StandardCharsets.UTF_8)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.fieldErrors").exists())
                .andExpect(jsonPath("$.details.fieldErrors.newUsername").exists());
    }

    @Test
    @DisplayName("사용자 삭제 성공")
    void user_delete_success() throws Exception {
        UUID userId = createUser("user3", "password1234", "user3@naver.com");

        mockMvc.perform(delete("/api/users/{userId}", userId))
                .andExpect(status().isNoContent());

    }

    @Test
    @DisplayName("사용자 삭제 실패")
    void user_delete_fail() throws Exception {
        mockMvc.perform(delete("/api/users/{userId}", UUID.randomUUID()))
                .andExpect(status().isNotFound());

    }
}
