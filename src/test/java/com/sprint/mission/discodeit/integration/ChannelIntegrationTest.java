package com.sprint.mission.discodeit.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.StatusAggregator;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ChannelIntegrationTest extends IntegrationBaseTest {

    @Test
    @DisplayName("PUBLIC 채널 생성 성공")
    void publicChannel_create_success() throws Exception {
        UUID publicChannel
                = createPublicChannel("channelName", "description", 0, "PUBLIC");
        assertThat(publicChannel).isNotNull();
    }

    @Test
    @DisplayName("PUBLIC 채널 생성 실패")
    void publicChannel_create_fail() throws Exception {
        String bad = """
                {
                "name": "",
                "description": "description",
                "slowModeSeconds": 0,
                "type": "PUBLIC"
                }
                """;

        mockMvc.perform(
                post("/api/channels/public")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bad)
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.details.fieldErrors").exists())
                .andExpect(jsonPath("$.details.fieldErrors.name").exists());
    }
    
    @Test
    @DisplayName("public 채널 수정 성공")
    void channel_update_public_success() throws Exception {
        UUID channelId = createPublicChannel(
                "channel2", "description", 0, "PUBLIC");

        String updateJson = """
                {
                "newName": "newChannel",
                "newDescription": "newDescription",
                "slowModeSeconds": 3
                }
                """;

        mockMvc.perform(
                patch("/api/channels/{channelId}", channelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(channelId.toString()))
                .andExpect(jsonPath("$.name").value("newChannel"));
    }

    @Test
    @DisplayName("채널 수정 실패")
    void channel_update_fail() throws Exception {
        UUID user1 = createUser("user1", "password1234", "user1@naver.com");
        UUID user2 = createUser("user2", "password1234", "user2@naver.com");

        UUID channelId = createPrivateChannel("PRIVATE", 0, user1, user2);

        String updateJson = """
                {
                "newName": "newName",
                "newDescription": "newDescription",
                "slowModeSeconds": 1
                }
                """;

        mockMvc.perform(
                patch("/api/channels/{channelId}",channelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson)
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("채널 삭제 성공")
    void channel_delete_success() throws Exception {
        UUID channelId = createPublicChannel(
                "channel3", "description", 0, "PUBLIC");

        mockMvc.perform(delete("/api/channels/{channelId}", channelId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("채널 삭제 실패")
    void channel_delete_fail() throws Exception {
        mockMvc.perform(delete("/api/channels/{channelId}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}

