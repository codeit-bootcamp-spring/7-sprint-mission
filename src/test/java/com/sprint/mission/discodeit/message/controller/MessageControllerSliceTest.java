package com.sprint.mission.discodeit.message.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.sprint.mission.discodeit.TestFixture;
import com.sprint.mission.discodeit.controller.MessageController;
import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.message.MessageDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MessageController.class)
@ActiveProfiles("test")
@DisplayName("Message Controller Slice Test")
public class MessageControllerSliceTest {

    private static final Logger log = LoggerFactory.getLogger(MessageControllerSliceTest.class);
    @MockitoBean
    MessageService messageService;

    @MockitoBean
    ChannelService channelService;

    @Autowired
    MockMvc mockMvc;

    private MessageDto messageDto;
    private User user;
    private Channel channel;
    private Message message;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(
            new ParameterNamesModule()
    );

    @BeforeEach
    void setUp() {
        user = User.createUserFactory(
                "user1","111@user","1234"
        );
        channel = Channel.publicChannelFactory("publicChannel","publicChannelDesc");
        message = Message.createMessageFactory(
                "siuuu",user,channel
        );
    }

    @Test
    @DisplayName("[정상 케이스] 메세지 생성 성공")
    void createMessage_Success() throws Exception {

        MessageCreateRequestDto request = TestFixture.messageCreateFactory(UUID.randomUUID(), UUID.randomUUID());
        MockMultipartFile jsonPart = new MockMultipartFile(
                "messageCreateRequest",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(request)
        );
        given(messageService.createMessage(any(MessageCreateRequestDto.class)
        ,any()
        )).willReturn(messageDto);

        mockMvc.perform( multipart("/api/messages")
                        .file(jsonPart)
                .contentType(MediaType.MULTIPART_FORM_DATA)
        )
                .andExpect(status().isCreated());

        then(messageService).should(times(1))
                .createMessage(any(MessageCreateRequestDto.class),any());

    }

    @Test
    @DisplayName("[예외 케이스] 메세지 생성 실패")
    void createMessage_Fail() throws Exception {

        MessageCreateRequestDto request = TestFixture.messageCreateFactory(null, UUID.randomUUID());
        MockMultipartFile jsonPart = new MockMultipartFile(
                "messageCreateRequest",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(request)
        );
        given(messageService.createMessage(any(MessageCreateRequestDto.class)
                ,any()
        )).willReturn(messageDto);

        mockMvc.perform( multipart("/api/messages")
                        .file(jsonPart)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.authorId").value(containsString("작성자")))
                .andDo(result -> log.info(result.getResponse().getContentAsString()));

        then(messageService).should(never()).createMessage(any(MessageCreateRequestDto.class),any());

    }

    @Test
    @DisplayName("[정상 케이스] 메세지 삭제 성공")
    void deleteMessage_Success() throws Exception {

        willDoNothing().given(messageService).deleteMessage(any(UUID.class));
        mockMvc.perform(delete("/api/messages/{messageId}",UUID.randomUUID()))
                .andExpect(status().isOk())
                .andDo(result -> log.info(result.getResponse().getContentAsString()));
        then(messageService).should(times(1)).deleteMessage(any(UUID.class));

    }

    @Test
    @DisplayName("[정상 케이스] 메세지 삭제 실패")
    void deleteMessage_Fail() throws Exception {

        willDoNothing().given(messageService).deleteMessage(any(UUID.class));
        mockMvc.perform(delete("/api/messages/{messageId}",111))
                .andExpect(status().isInternalServerError())
                .andDo(result -> log.info(result.getResponse().getContentAsString()));
        then(messageService).should(never()).deleteMessage(any(UUID.class));
    }
}
