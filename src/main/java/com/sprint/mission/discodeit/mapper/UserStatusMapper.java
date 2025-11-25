package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.userstatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserStatusMapper {
    public UserStatusResponseDto toDto(UserStatus userStatus) {
        if (userStatus == null) {
            throw new IllegalArgumentException("userStatus is null");
        }
        return new UserStatusResponseDto(
                userStatus.getId(),
                userStatus.getUser().getId(),
                userStatus.getLastActiveAt()
        );
    }

    public List<UserStatusResponseDto> toDtoList(List<UserStatus> userStatuses) {
        if (userStatuses == null || userStatuses.isEmpty()) {
            return List.of();
        }

        return userStatuses.stream()
                .map(userStatus -> toDto(userStatus))
                .toList();
    }
}
