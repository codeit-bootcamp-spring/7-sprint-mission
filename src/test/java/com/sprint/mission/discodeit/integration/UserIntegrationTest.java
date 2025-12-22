package com.sprint.mission.discodeit.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.user.request.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // 실제 HTTP 요청/응답
@ActiveProfiles("test")
//@Transactional // RANDOM_PORT와 함께 사용시 트랜잭션이 달라져서 제외
public class UserIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserStatusRepository userStatusRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/users";
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("사용자를 프로필과 함께 생성할 수 있다")
    void createUser_WithProfile_Success() throws Exception {
        // given
        CreateUserRequestDto requestDto = new CreateUserRequestDto(
                "test@naver.com",
                "testuser",
                "test1234"
        );

        // JSON part를 MultiValueMap에 넣어 직렬화
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("userCreateRequest", new HttpEntity<>(
                objectMapper.writeValueAsString(requestDto),
                createJsonHeaders()
        ));

        // 파일 part
        Resource profileResource = new ByteArrayResource("dummy image content".getBytes()) {
            @Override
            public String getFilename() {
                return "profile.png";
            }
        };
        body.add("profile", profileResource);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // when
        ResponseEntity<UserResponseDto> response = restTemplate.postForEntity(
                baseUrl,
                requestEntity,
                UserResponseDto.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().username()).isEqualTo("testuser");
        assertThat(response.getBody().email()).isEqualTo("test@naver.com");
    }

    @Test
    @DisplayName("이메일 형식을 맞추지 않으면 사용자를 생성할 수 없다")
    void createUser_WithInvalidEmail_Fail() throws Exception {
        // given
        CreateUserRequestDto requestDto = new CreateUserRequestDto(
                "test",
                "testuser",
                "test1234"
        );

        // JSON part를 MultiValueMap에 넣어 직렬화
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("userCreateRequest", new HttpEntity<>(
                objectMapper.writeValueAsString(requestDto),
                createJsonHeaders()
        ));

        // 파일 part
        Resource profileResource = new ByteArrayResource("dummy image content".getBytes()) {
            @Override
            public String getFilename() {
                return "profile.png";
            }
        };
        body.add("profile", profileResource);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // when
        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl,
                requestEntity,
                String.class
        );

        // then
        JsonNode json = objectMapper.readTree(response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(json.get("exceptionType").asText()).isEqualTo("MethodArgumentNotValidException");
        assertThat(json.get("message").asText()).isEqualTo("요청 데이터가 유효하지 않습니다.");
        assertThat(json.get("details").get("email").asText()).isEqualTo("올바른 이메일 형식이 아닙니다.");
    }

    @Test
    @DisplayName("사용자 정보를 프로필 없이 수정할 수 있다")
    void updateUser_WithoutProfile_Success() throws Exception {
        // given
        User user = new User(
                "testuser2",
                "test2@naver.com",
                "test1234",
                null
        );
        User saved = userRepository.save(user);
        userStatusRepository.save(new UserStatus(user));

        // 업데이트 DTO
        UpdateUserRequestDto updateRequest = new UpdateUserRequestDto(
                "updated2@naver.com",
                "updatedUser2",
                null
        );

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("userUpdateRequest", new HttpEntity<>(
                objectMapper.writeValueAsString(updateRequest),
                createJsonHeaders()
        ));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // when
        ResponseEntity<UserResponseDto> response = restTemplate.exchange(
                baseUrl + "/" + saved.getId(),
                HttpMethod.PATCH,
                requestEntity,
                UserResponseDto.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().username()).isEqualTo("updatedUser2");
        assertThat(response.getBody().email()).isEqualTo("updated2@naver.com");
    }

    private HttpHeaders createJsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
