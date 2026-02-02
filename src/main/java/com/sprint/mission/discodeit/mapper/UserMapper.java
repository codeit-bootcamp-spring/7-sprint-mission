package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binaryContentDto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.userDto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final BinaryContentMapper binaryContentMapper;

    public UserDto toDto(User user) {

        // Optional = false
        boolean isOnline = user.getStatus().isOnline();
        // BinaryMapper내부에서 검사
        BinaryContentDto profile = binaryContentMapper.toDto(user.getProfile());

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                profile,
                isOnline,
                user.getRole()
        );
    }
}

