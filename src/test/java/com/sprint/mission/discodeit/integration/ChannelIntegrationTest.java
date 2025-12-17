package com.sprint.mission.discodeit.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.channel.request.CreatePublicChannelRequestDto;
import com.sprint.mission.discodeit.dto.channel.response.ChannelDto;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ChannelIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/channels/public";
        channelRepository.deleteAll();
    }

    @Test
    @DisplayName("공개 채널을 생성할 수 있다")
    void createPublicChannel_Success() {
        // given

        String name = "channelName";
        String description = "description";

        CreatePublicChannelRequestDto requestDto =
                new CreatePublicChannelRequestDto(
                        name,
                        description
                );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreatePublicChannelRequestDto> requestEntity =
                new HttpEntity<>(requestDto, headers);

        // when
        ResponseEntity<ChannelDto> response = restTemplate.postForEntity(
                baseUrl,
                requestEntity,
                ChannelDto.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().name()).isEqualTo(name);
        assertThat(response.getBody().description()).isEqualTo(description);

        // DB까지 실제로 저장됐는지 확인
        assertThat(channelRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("채널 이름이 비어 있으면 공개 채널 생성에 실패한다")
    void createPublicChannel_InvalidName_Fail() throws Exception {
        // given
        CreatePublicChannelRequestDto invalidRequest =
                new CreatePublicChannelRequestDto(
                        "", null);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreatePublicChannelRequestDto> requestEntity =
                new HttpEntity<>(invalidRequest, headers);

        // when
        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl,
                requestEntity,
                String.class
        );

        // then
        JsonNode json = objectMapper.readTree(response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(json.get("code").asText()).isEqualTo("VALIDATION_ERROR");
        assertThat(json.get("exceptionType").asText()).isEqualTo("MethodArgumentNotValidException");
        assertThat(json.get("message").asText()).isEqualTo("요청 데이터가 유효하지 않습니다.");
        assertThat(json.get("details").get("name").asText()).isEqualTo("채널 이름은 필수 값입니다.");
    }
}
