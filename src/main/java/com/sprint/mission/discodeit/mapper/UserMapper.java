package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final BinaryContentMapper binaryContentMapper;

    public UserDto toDto(User user){
        BinaryContentDto binaryContentDto = binaryContentMapper.toDto(user.getProfile());
        return new UserDto(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                binaryContentDto,
                binaryContentDto.id(),
                user.getUserStatus().isUserOnline()
        );
    }
}
