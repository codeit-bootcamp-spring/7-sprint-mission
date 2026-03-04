package com.sprint.mission.discodeit.fixture;

import com.sprint.mission.discodeit.dto.userDto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.userDto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.enums.Role;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

public class UserFixture {

    public static final String PASSWORD = "Password123!";

    // Request ========================================================
    public static UserCreateRequest getUserRequest(int index) {
        return UserCreateRequest.builder()
                .username("user" + index)
                .email("user" + index + "@discodeit.com")
                .password(PASSWORD)
                .build();
    }
    public static UserCreateRequest getAdminRequest() {
        return UserCreateRequest.builder()
                .username("Admin")
                .email("admin@discodeit.com")
                .password(PASSWORD)
                .build();
    }
    public static UserCreateRequest getChannelManagerRequest() {
        return UserCreateRequest.builder()
                .username("CM")
                .email("cm@discodeit.com")
                .password(PASSWORD)
                .build();
    }

    // Entity ========================================================

    public static User getUser(int index) {
        return toEntity(getUserRequest(index), Role.USER);
    }

    public static User getAdmin() {
        return toEntity(getAdminRequest(), Role.ADMIN);
    }

    public static User getChannelManager() {
        return toEntity(getChannelManagerRequest(), Role.CHANNEL_MANAGER);
    }


    private static User toEntity(UserCreateRequest request, Role role) {
        User user =  User.builder()
                .email(request.email())
                .username(request.username())
                .password(request.password())
                .role(role)
                .build();

        ReflectionTestUtils.setField(user, "id", UUID.randomUUID());
        return user;
    }

    // Response ========================================================

    public static UserDto getUserDto(int index) {
        return toDto(getUser(index));
    }
    public static UserDto getAdminDto() {
        return toDto(getAdmin());
    }
    public static UserDto getChannelManagerDto() {
        return toDto(getChannelManager());
    }

    private static UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .online(false)
                .build();
    }
}
