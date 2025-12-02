package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final BinaryContentMapper binaryContentMapper;

    public UserResponseDto toResponseDto(User user){
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                binaryContentMapper.toResponseDto(user.getProfile()),
                user.getStatus().isOnline()
        );
    }
}
