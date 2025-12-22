package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.channel.request.CreatePublicChannelRequestDto;
import com.sprint.mission.discodeit.dto.channel.response.ChannelDto;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChannelController.class)
class ChannelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ChannelService channelService;

    @Test
    @DisplayName("공개 채널 생성 - 성공")
    void createPublicChannel_Success() throws Exception {
        // given

        String name = "channel";
        String description = "description";

        CreatePublicChannelRequestDto requestDto
                = new CreatePublicChannelRequestDto(name, description);

        ChannelDto responseDto = new ChannelDto(
                UUID.randomUUID(),
                ChannelType.PUBLIC,
                name,
                description,
                new ArrayList<>(),
                null
        );

        when(channelService.create(any(CreatePublicChannelRequestDto.class)))
                .thenReturn(responseDto);

        // when & then
        mockMvc.perform(post("/api/channels/public")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(responseDto.id().toString()))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.type").value(ChannelType.PUBLIC.name()));
    }

    @Test
    @DisplayName("공개 채널 생성 - 이름 누락으로 실패")
    void createPublicChannel_Fail_ValidationError() throws Exception {
        // given: 이름 누락
        CreatePublicChannelRequestDto requestDto =
                new CreatePublicChannelRequestDto(null, "description");

        // when & then
        mockMvc.perform(post("/api/channels/public")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.details.name").value("채널 이름은 필수 값입니다."));
    }
}