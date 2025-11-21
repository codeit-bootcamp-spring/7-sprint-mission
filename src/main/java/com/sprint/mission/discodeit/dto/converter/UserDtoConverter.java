package com.sprint.mission.discodeit.dto.converter;

import com.sprint.mission.discodeit.dto.binarycontent.Response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserDtoConverter {
    public UserResponseDto toResponseDto(User user, boolean online, BinaryContentResponseDto binaryContentResponseDto){
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                binaryContentResponseDto,
                online
        );
    }
}
