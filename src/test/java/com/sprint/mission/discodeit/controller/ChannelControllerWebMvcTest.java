package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.common.exception.GlobalExceptionHandler;
import com.sprint.mission.discodeit.dto.request.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ChannelController.class)
@ActiveProfiles("test")
@Import(GlobalExceptionHandler.class)
class ChannelControllerWebMvcTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    ChannelService channelService;

    @Test
    @DisplayName("POST: /api/channels/public 성공")
    void createPublic_success() throws Exception {
        // given
        PublicChannelCreateRequestDto publicChannelCreateRequestDto = new PublicChannelCreateRequestDto(
                "public-channel",
                "description",
                0,
                ChannelType.PUBLIC
        );
        given(channelService.createPublic(any(PublicChannelCreateRequestDto.class))).willReturn(null);

        // when & then
        mockMvc.perform(
                post("/api/channels/public")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(publicChannelCreateRequestDto))
        )
                .andExpect(status().isCreated());

        then(channelService).should().createPublic(any(PublicChannelCreateRequestDto.class));

    }

    @Test
    @DisplayName("POST: /api/channels/public 실패")
    void createPublic_fail() throws Exception {
        // given
        PublicChannelCreateRequestDto publicChannelCreateRequestDto = new PublicChannelCreateRequestDto(
                "",
                "description",
                0,
                ChannelType.PUBLIC
        );

        // when & then
        mockMvc.perform(
                post("/api/channels/public")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(publicChannelCreateRequestDto))
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.details.fieldErrors").exists())
                .andExpect(jsonPath("$.details.fieldErrors.name").exists());


    }
}
