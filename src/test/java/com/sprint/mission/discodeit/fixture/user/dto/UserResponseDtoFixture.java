package com.sprint.mission.discodeit.fixture.user.dto;

import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.enums.Role;

import java.util.UUID;

public class UserResponseDtoFixture {
    public static UserResponseDto createUserResponse(UUID userId, String email, String username) {
        return new UserResponseDto(
                userId,
                username,
                email,
                null,
                true,
                Role.USER
        );
    }

    public static UserResponseDto createUserResponseWithProfile(UUID userId, String email, String username, BinaryContentResponseDto binaryContentResponseDto) {
        return new UserResponseDto(
                userId,
                username,
                email,
                binaryContentResponseDto,
                true,
                Role.USER
        );
    }
}
