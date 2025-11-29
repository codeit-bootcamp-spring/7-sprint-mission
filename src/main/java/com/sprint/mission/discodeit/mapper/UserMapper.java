package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.mapper.dto.BinaryContentDto;
import com.sprint.mission.discodeit.mapper.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final BinaryContentMapper binaryContentMapper;

    public UserDto toDto(User user) {

        BinaryContentDto profile = (null != user.getProfile()) ? binaryContentMapper.toDto(user.getProfile()) :  null;

        return UserDto.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .profile(profile)
            .online(user.getUserStatus().isOnline())
            .build();
    }
}
