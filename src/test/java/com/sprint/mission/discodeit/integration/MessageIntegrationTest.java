package com.sprint.mission.discodeit.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.response.MessageResponseDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class MessageIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserStatusRepository userStatusRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/messages";
        userStatusRepository.deleteAll();
        userRepository.deleteAll();
        messageRepository.deleteAll();
        channelRepository.deleteAll();

    }

    @Test
    @DisplayName("첨부파일 없이 메시지를 생성할 수 있다")
    void createMessage_WithoutAttachments_Success() throws Exception {
        // given
        String content = "content";

        User user = userRepository.save(
                new User("username", "email@email.com", "qwer1234", null)
        );
        userStatusRepository.save(new UserStatus(user));

        Channel channel = channelRepository.save(
                new Channel("channelName", ChannelType.PUBLIC, null)
        );

        CreateMessageRequestDto requestDto =
                new CreateMessageRequestDto(
                        content,
                        channel.getId(),
                        user.getId()
                );

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("messageCreateRequest", new HttpEntity<>(
                objectMapper.writeValueAsString(requestDto),
                jsonHeaders()
        ));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity =
                new HttpEntity<>(body, headers);

        // when
        ResponseEntity<MessageResponseDto> response =
                restTemplate.postForEntity(
                        baseUrl,
                        requestEntity,
                        MessageResponseDto.class
                );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().content()).isEqualTo(content);
        assertThat(messageRepository.count()).isEqualTo(1);
    }

    private HttpHeaders jsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Test
    @DisplayName("첨부파일과 함께 메시지를 생성할 수 있다")
    void createMessage_WithAttachments_Success() throws Exception {
        // given
        String content = "content";

        User user = userRepository.save(
                new User("username", "email@email.com", "qwer1234", null)
        );
        userStatusRepository.save(new UserStatus(user));

        Channel channel = channelRepository.save(
                new Channel("channelName", ChannelType.PUBLIC, null)
        );

        CreateMessageRequestDto requestDto =
                new CreateMessageRequestDto(
                        content,
                        channel.getId(),
                        user.getId()
                );

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("messageCreateRequest", new HttpEntity<>(
                objectMapper.writeValueAsString(requestDto),
                jsonHeaders()
        ));

        MockMultipartFile file1 = new MockMultipartFile(
                "attachments",
                "file1.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "file1".getBytes()
        );

        MockMultipartFile file2 = new MockMultipartFile(
                "attachments",
                "file2.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "file2".getBytes()
        );

        body.add("attachments", file1.getResource());
        body.add("attachments", file2.getResource());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity =
                new HttpEntity<>(body, headers);

        // when
        ResponseEntity<MessageResponseDto> response =
                restTemplate.postForEntity(
                        baseUrl,
                        requestEntity,
                        MessageResponseDto.class
                );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().content()).isEqualTo(content);
        assertThat(messageRepository.count()).isEqualTo(1);
        assertThat(response.getBody().attachments()).hasSize(2);
    }

    @Test
    @DisplayName("메시지 내용이 비어 있으면 수정에 실패한다")
    void updateMessage_EmptyContent_Fail() throws Exception {
        // given
        User user = userRepository.save(
                new User("username", "email@email.com", "qwer1234", null)
        );
        userStatusRepository.save(new UserStatus(user));

        Channel channel = channelRepository.save(
                new Channel("channelName", ChannelType.PUBLIC, null)
        );

        Message message = messageRepository.save(
                new Message("content", channel, user, new ArrayList<>())
        );

        UpdateMessageRequestDto requestDto =
                new UpdateMessageRequestDto("");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(
                objectMapper.writeValueAsString(requestDto),
                headers
        );

        // when
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/" + message.getId(),
                HttpMethod.PATCH,
                requestEntity,
                String.class
        );

        // then
        JsonNode json = objectMapper.readTree(response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(json.get("exceptionType").asText()).isEqualTo("MethodArgumentNotValidException");
        assertThat(json.get("message").asText()).isEqualTo("요청 데이터가 유효하지 않습니다.");
        assertThat(json.get("details").get("newContent").asText()).isEqualTo("메시지 내용은 비어 있을 수 없습니다.");
    }
}
