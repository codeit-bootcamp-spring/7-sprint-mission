package com.sprint.mission.discodeit.dto.fileIo.mapper;

import com.sprint.mission.discodeit.dto.fileIo.UserStatusIoDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.common.exceptions.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;

final class UserStatusMapper {
    private UserStatusMapper() {
    }

    public static UserStatusIoDTO toDto(UserStatus userStatus) {
        return new UserStatusIoDTO(
                userStatus.getUuid(),
                userStatus.getCreatedAt(),
                userStatus.getUpdatedAt(),
                userStatus.getUser().getUuid(),
                userStatus.getOnlineStatus()
        );
    }

    public static UserStatus toUserStatus(UserStatusIoDTO dto, UserRepository userRepository) {
        User user = userRepository.find(dto.getUserUuid())
                .orElseThrow(() -> new UserNotFoundException(dto.getUserUuid()));
        return new UserStatus(user, dto.getOnlineStatus());
    }
}

