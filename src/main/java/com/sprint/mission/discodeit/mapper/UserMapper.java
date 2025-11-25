package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.response.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final BinaryContentMapper binaryContentMapper;

    public UserResponseDto toDto(User user, boolean online) {
        if(user == null) {
            throw new IllegalArgumentException("user must not be null.");
        }

        BinaryContentResponseDto profileDto = null;
        if(user.getProfile() != null) {
            profileDto = binaryContentMapper.toDto(user.getProfile());
        }

        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                profileDto,
                online
        );
    }
}
