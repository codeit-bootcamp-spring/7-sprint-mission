package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.response.MessageResponseDto;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.service.MessageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BinaryContentMapper binaryContentMapper;

    @MockitoBean
    private MessageService messageService;

    @Test
    @DisplayName("메시지 수정 - 성공")
    void updateMessage_Success() throws Exception {
        // given
        UUID messageId = UUID.randomUUID();
        UpdateMessageRequestDto requestDto = new UpdateMessageRequestDto("수정된 메시지 내용");

        MessageResponseDto responseDto = new MessageResponseDto(
                messageId,
                Instant.now(),
                Instant.now(),
                "수정된 메시지 내용",
                UUID.randomUUID(),
                null,
                null
        );

        when(messageService.update(eq(messageId), any(UpdateMessageRequestDto.class)))
                .thenReturn(responseDto);

        // when & then
        mockMvc.perform(patch("/api/messages/{messageId}", messageId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(messageId.toString()))
                .andExpect(jsonPath("$.content").value("수정된 메시지 내용"));
    }

    @Test
    @DisplayName("메시지 수정 - 내용 누락으로 실패")
    void updateMessage_Fail_ValidationError() throws Exception {
        // given
        UUID messageId = UUID.randomUUID();
        UpdateMessageRequestDto requestDto = new UpdateMessageRequestDto(""); // 내용 빈 문자열

        // when & then
        mockMvc.perform(patch("/api/messages/{messageId}", messageId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.details.newContent").value("메시지 내용은 비어 있을 수 없습니다."));
    }
}