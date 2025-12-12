package com.sprint.mission.discodeit.channel.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.sprint.mission.discodeit.TestFixture;
import com.sprint.mission.discodeit.controller.ChannelController;
import com.sprint.mission.discodeit.dto.request.channel.ChannelPatchRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.ChannelPrivateCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.ChannelPublicCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.channel.ChannelDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entityElement.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChannelController.class)
@ActiveProfiles("test")
@DisplayName("Channel Controller Slice Test")
public class ChannelControllerSliceTest {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ChannelControllerSliceTest.class);
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ChannelService channelService;



    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new ParameterNamesModule());
    private ChannelDto channelDto;

    @BeforeEach
    void setUp() {
        channelDto = new ChannelDto(
                UUID.randomUUID(),
                ChannelType.PUBLIC,
                "test",
                "test description",
                null,
                Instant.now()
        );
    }

    @Test
    @DisplayName("[정상 케이스] 공용 채널 생성 성공")
    void createChannel_Success() throws Exception {

        //given
        ChannelDto channelDto = new ChannelDto(
                UUID.randomUUID(),
                ChannelType.PUBLIC,
                "test",
                "test description",
                null,
                Instant.now()
        );

        ChannelPublicCreateRequestDto request = TestFixture.channelPublicCreateFactory();
        given(channelService.createPublicChannel(any(ChannelPublicCreateRequestDto.class))).willReturn(channelDto);
        mockMvc.perform(post("/api/channels/public")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        then(channelService).should(times(1))
                .createPublicChannel(any(ChannelPublicCreateRequestDto.class));

    }

    @Test
    @DisplayName("[예외 케이스] 공용 채널 생성 실패")
    void createChannel_Fail() throws Exception {

        //given

        ChannelPublicCreateRequestDto request = new ChannelPublicCreateRequestDto(null,null);
        given(channelService.createPublicChannel(any(ChannelPublicCreateRequestDto.class))).willReturn(channelDto);
        mockMvc.perform(post("/api/channels/public")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.name").value("Public Channel name"))
                        .andDo(result -> log.info(result.getResponse().getContentAsString()));

        then(channelService).should(never())
                .createPublicChannel(any(ChannelPublicCreateRequestDto.class));


    }

    @Test
    @DisplayName("[정상 케이스] 채널 변경 성공")
    void patchChannel_Success() throws Exception {
        given(channelService.patchChannel(any(ChannelPatchRequestDto.class)
        ,any(UUID.class)
        )).willReturn(channelDto);
        ChannelPatchRequestDto request = TestFixture.channelPatchFactory();
        mockMvc.perform(patch("/api/channels/"+UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()));

        then(channelService).should(times(1))
                .patchChannel(any(ChannelPatchRequestDto.class),any(UUID.class));


    }

    @Test
    @DisplayName("[예외 케이스] 채널 변경 실패")
    void patchChannel_Fail() throws Exception {

        given(channelService.patchChannel(any(ChannelPatchRequestDto.class)
                ,any(UUID.class)
        )).willReturn(channelDto);
        String mismatch = "hello";
        mockMvc.perform(patch("/api/channels/"+UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mismatch)))
                .andExpect(status().isInternalServerError())
                .andDo(result -> log.info(result.getResponse().getContentAsString()));

        then(channelService).should(never())
                .patchChannel(any(ChannelPatchRequestDto.class),any(UUID.class));
    }

}
