package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.status.UserActiveStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserMapperManual {

    private final BinaryContentMapperManual binaryContentMapper;

    public UserResponseDto toDto(User user) {
        if (user == null) return null;
        BinaryContentResponseDto binaryContentResponseDto = Optional.ofNullable(user.getProfile()) // TODO: lazy가 나을지 eager가 나을지 혹은 feth join?
                .map(binaryContentMapper::toDto)
                .orElse(null);

        UserActiveStatus userActiveStatus = Optional.ofNullable(user.getUserStatus()) // TODO: lazy가 나을지 eager가 나을지 혹은 feth join?
                .map(UserStatus::getUserStatus)
                .orElse(UserActiveStatus.OFFLINE);

        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                binaryContentResponseDto,
                userActiveStatus == UserActiveStatus.ONLINE,
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

}
