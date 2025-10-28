package com.sprint.mission.discodeit.application.dto;

import com.sprint.mission.discodeit.application.dto.response.UserResponseDto;
import com.sprint.mission.discodeit.domain.user.User;
import org.springframework.stereotype.Service;


public final class UserDtoMapper {

    private UserDtoMapper(){}

    public static UserResponseDto userToResponseDto(User user) {
        return new UserResponseDto(user.getEmail(), user.getUsername(), user.getPhoneNumber());
    }

}
