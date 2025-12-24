package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.common.exception.GlobalExceptionHandler;
import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.response.page.PageResponseDto;
import com.sprint.mission.discodeit.dto.response.user.UserResponseDto;
import com.sprint.mission.discodeit.service.MessageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MessageController.class)
@ActiveProfiles("test")
@Import(GlobalExceptionHandler.class)
class MessageControllerWebMvcTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    MessageService messageService;

    @Test
    @DisplayName("GET /api/messages 성공")
    void getMessages_success() throws Exception{
        // given
        UUID channelId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        UUID messageId = UUID.randomUUID();

        UserResponseDto userResponseDto
                = new UserResponseDto(authorId, "user", "user@naver.com", null, true);

        MessageResponseDto messageResponseDto = new MessageResponseDto(
                messageId,
                "hi",
                userResponseDto,
                channelId,
                List.of(),
                Instant.now(),
                Instant.now()
        );

        PageResponseDto<MessageResponseDto> pageResponseDto = new PageResponseDto<>(
                List.of(messageResponseDto),
                0,
                50,
                false,
                null
        );

        given(messageService.getPageByChannelId(eq(channelId),any())).willReturn(pageResponseDto);

        // when & then
        mockMvc.perform(
                get("/api/messages")
                        .param("channelId", channelId.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(messageId.toString()))
                .andExpect(jsonPath("$.content[0].content").value("hi"))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(50))
                .andExpect(jsonPath("$.hasNext").value(false));


    }

    @Test
    @DisplayName("POST: /api/messages 실패")
    void createMessages_fail() throws Exception{
        // given
        UUID channelId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();

        MessageCreateRequestDto messageCreateRequestDto
                = new MessageCreateRequestDto("", authorId, channelId);

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "messageCreateRequest",
                "messageCreateRequest",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(messageCreateRequestDto)
        );

        // when & then
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
}
