package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final BinaryContentMapper binaryContentMapper;

    public UserDto toDto(User user){
        BinaryContentDto binaryContentDto = user.getProfile()==null ?null:binaryContentMapper.toDto(user.getProfile());
        UUID binaryContentId = binaryContentDto==null?null:binaryContentDto.id();
        return new UserDto(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                binaryContentDto,
                binaryContentId,
                user.getUserStatus().isUserOnline()
        );
    }
}
