package com.sprint.mission.discodeit.integration;

import com.sprint.mission.discodeit.dto.userDto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.userDto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserIntegrationTest extends IntegrationTest{

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("유저를 생성할 수 있다.")
    @Transactional
    void createUser() throws Exception {
        // given
        UserCreateRequest request = UserCreateRequest.builder()
                .email("test@codeit.com")
                .password("Password1!")
                .username("test")
                .build();
        String json = objectMapper.writeValueAsString(request);
        MockMultipartFile multipartFile
                = new MockMultipartFile("userCreateRequest", "",
                MediaType.APPLICATION_JSON_VALUE, json.getBytes());

        // when
        mockMvc.perform(multipart("/api/users")
                .file(multipartFile)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated());
        // then
        User savedUser = userRepository.findByEmail("test@codeit.com")
                .orElseThrow(() -> new UserNotFoundException("test@codeit.com"));
        assertThat(savedUser.getUsername()).isEqualTo("test");

    }

    @Test
    @DisplayName("유저 정보를 수정할 수 있다.")
    @Transactional
    void updateUser() throws Exception {
        // given
        User user = new User("old@codeit.com", "Password1!", "old");
        UserStatus userStatus = new UserStatus(user);
        user.updateStatus(userStatus);
        userRepository.save(user);

        UserUpdateRequest request = new UserUpdateRequest("new", "new@codeit.com", null);
        String json = objectMapper.writeValueAsString(request);
        MockMultipartFile multipartFile
                = new MockMultipartFile("userUpdateRequest", "",
                MediaType.APPLICATION_JSON_VALUE, json.getBytes());

        // when
        mockMvc.perform(multipart(HttpMethod.PATCH, "/api/users/" + user.getId())
                .file(multipartFile))
                .andExpect(status().isOk());

        // then
        User updatedUser = userRepository.findById(user.getId()).orElseThrow(() -> new UserNotFoundException(user.getId()));
        assertThat(updatedUser.getUsername()).isEqualTo("new");
        assertThat(updatedUser.getEmail()).isEqualTo("new@codeit.com");
    }
}
