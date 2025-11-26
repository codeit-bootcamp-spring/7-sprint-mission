package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.mapper.dto.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.stereotype.Component;

@Component
public class UserStatusMapper {
    public UserStatusDto toDto(UserStatus userStatus) {
        return UserStatusDto.builder()
            .user(userStatus.getUser())
            .lastActiveAt(userStatus.getLastActiveAt())
            .build();
    }
}
