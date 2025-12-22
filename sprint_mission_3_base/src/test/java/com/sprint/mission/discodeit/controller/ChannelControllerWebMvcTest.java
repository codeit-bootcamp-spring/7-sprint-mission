package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.config.WebMvcTestSupportConfig;
import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
        controllers = ChannelController.class,
        excludeAutoConfiguration = {
                DataSourceAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class,
                JpaRepositoriesAutoConfiguration.class,
                DataSourceTransactionManagerAutoConfiguration.class,
                TransactionAutoConfiguration.class
        }
)
@Import(WebMvcTestSupportConfig.class)
class ChannelControllerWebMvcTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    ChannelService channelService;

    @Test
    @DisplayName("POST /api/channels 성공: 채널 생성 -> 201 + JSON")
    void create_success() throws Exception {
        UUID channelId = UUID.randomUUID();

        ChannelCreateRequest request = new ChannelCreateRequest(
                ChannelType.PUBLIC,
                "general",
                List.of()
        );

        ChannelDto response = new ChannelDto(
                channelId,
                "general",
                List.of(),
                null
        );

        given(channelService.create(any(ChannelCreateRequest.class))).willReturn(response);

        mockMvc.perform(
                        post("/api/channels")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(channelId.toString()))
                .andExpect(jsonPath("$.name").value("general"));
    }

    @Test
    @DisplayName("POST /api/channels 실패: Validation 실패 -> 400 + fieldErrors")
    void create_validation_fail() throws Exception {
        String invalidJson = """
                {
                  "type": null,
                  "name": ""
                }
                """;

        mockMvc.perform(
                        post("/api/channels")
                                .contentType(APPLICATION_JSON)
                                .content(invalidJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.fieldErrors").exists());
    }

    @Test
    @DisplayName("PATCH /api/channels/{id} 실패: CHANNEL_NOT_FOUND -> 404 + ErrorResponse")
    void update_not_found() throws Exception {
        UUID channelId = UUID.randomUUID();

        willThrow(new DiscodeitException(ErrorCode.CHANNEL_NOT_FOUND))
                .given(channelService)
                .update(eq(channelId), any());

        String body = """
                {
                  "name": "new-name"
                }
                """;

        mockMvc.perform(
                        patch("/api/channels/{id}", channelId)
                                .contentType(APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ErrorCode.CHANNEL_NOT_FOUND.name()));
    }
}
