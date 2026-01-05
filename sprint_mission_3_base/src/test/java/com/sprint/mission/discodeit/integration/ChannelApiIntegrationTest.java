package com.sprint.mission.discodeit.integration;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class ChannelApiIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/channels 성공 -> 201")
    void createChannel_success() throws Exception {
        MvcResult result = mockMvc.perform(
                        post("/api/channels")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                  "type": "PUBLIC",
                                  "name": "channel-a",
                                  "description": "desc-a"
                                }
                                """)
                )
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("channel-a")))
                .andReturn();

        String body = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JsonNode json = objectMapper.readTree(body);
        UUID channelId = UUID.fromString(json.get("id").asText());

        mockMvc.perform(delete("/api/channels/{id}", channelId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/channels/{id} 실패(없는 채널) -> 404")
    void deleteChannel_notFound() throws Exception {
        mockMvc.perform(delete("/api/channels/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}
