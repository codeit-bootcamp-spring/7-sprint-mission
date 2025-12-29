package com.sprint.mission.discodeit.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.config.TestJpaAuditingConfig;
import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Import(TestJpaAuditingConfig.class)
public abstract class IntegrationBaseTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected MockMultipartHttpServletRequestBuilder multipartPatch(String url, UUID uri) {
        return multipart(HttpMethod.PATCH, url, uri);
    }

    protected UUID createUser(String username, String password, String email) throws Exception {
        UserCreateRequestDto userCreateRequestDto = new UserCreateRequestDto(username, password, email);

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "userCreateRequest",
                "userCreateRequest",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(userCreateRequestDto)
        );

        String content = mockMvc.perform(
                        multipart("/api/users")
                                .file(mockMultipartFile)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        return extractId(content, "id");
    }

    protected UUID createPublicChannel(String name, String description, Integer slowModeSeconds, String type) throws Exception {
        String json = """
                {
                "name": "%s",
                "description": %s,
                "slowModeSeconds": %s,
                "type": "%s"
                }
                """.formatted(name, description == null ? "null" : "\"" + escapeJson(description) + "\"",
                slowModeSeconds == null ? "null" : slowModeSeconds, type);

        String content = mockMvc.perform(
                        post("/api/channels/public")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        return extractId(content, "id");
    }

    protected UUID createPrivateChannel(String type, Integer slowModeSeconds, UUID userId1, UUID userId2) throws Exception {
        String json = """
                {
                "participantIds": ["%s","%s"],
                "slowModeSeconds": %s,
                "type": "%s"
                }
                """.formatted(userId1, userId2,
                slowModeSeconds == null ? "null" : slowModeSeconds, type);

        String content = mockMvc.perform(
                        post("/api/channels/private")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        return extractId(content, "id");
    }

    protected UUID createMessage(UUID channelId, UUID authorId, String content) throws Exception {
        MessageCreateRequestDto messageCreateRequestDto
                = new MessageCreateRequestDto(content, authorId, channelId);

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "messageCreateRequest",
                "messageCreateRequest",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(messageCreateRequestDto)
        );

        String mockContent = mockMvc.perform(
                        multipart("/api/messages")
                                .file(mockMultipartFile)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        return extractId(mockContent, "id");
    }

    private UUID extractId(String json, String id) throws Exception {
        JsonNode jsonRoot = objectMapper.readTree(json);
        JsonNode jsonNode = jsonRoot.get(id);
        if (jsonNode == null || jsonNode.isNull()) {
            throw new IllegalStateException("Not response field");
        }
        return UUID.fromString(jsonNode.asText());
    }

    private static String escapeJson(String str) {
        return str.replace("\\", "\\\\").replace("\"", "\\\"");
    }

}
