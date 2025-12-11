package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.mapper.dto.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.stereotype.Component;

@Component
public class UserStatusMapper {
    public UserStatusDto toDto(UserStatus userStatus) {
        if (null == userStatus) {
            return null;
        }

        return UserStatusDto.builder()
            .id(userStatus.getId())
            .userId(userStatus.getUser().getId())
            .lastActiveAt(userStatus.getLastActiveAt())
            .build();
    }
}
