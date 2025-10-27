package com.sprint.mission.discodeit.entity.dto.fileIo.mapper;

import com.sprint.mission.discodeit.entity.base.User;
import com.sprint.mission.discodeit.entity.dto.fileIo.UserIoDTO;
import com.sprint.mission.discodeit.enums.OnlineStatus;

final class UserMapper {
    private UserMapper() {}

    public static User toUser(UserIoDTO dto) {
        return User.fromDto(
                dto.getUuid(),
                dto.getCreatedAt(),
                dto.getUpdatedAt(),
                dto.getUserId(),
                dto.getPasswd(),
                dto.getDisplayName(),
                dto.getBio(),
                OnlineStatus.valueOf(dto.getOnlineStatus())
        );
    }

    public static UserIoDTO toDto(User user) {
        return new UserIoDTO(
                user.getUuid(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getUserId(),
                user.getPasswd(),
                user.getDisplayName(),
                user.getBio(),
                user.getOnlineStatus().name()
        );
    }
}

