package com.sprint.mission.discodeit.fixture.user.dto;

import com.sprint.mission.discodeit.dto.user.request.CreateUserDto;

public class CreateUserDtoFixture {
    public static CreateUserDto createUserDto() {
        return new CreateUserDto(
                "test",
                "test_123",
                "test@codeit.com"
        );
    }

    public static CreateUserDto createUserDto(String username) {
        return new CreateUserDto(
                username,
                username + "_password",
                username + "test@codeit.com"
        );
    }

}
