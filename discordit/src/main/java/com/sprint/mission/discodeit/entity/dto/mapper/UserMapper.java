package com.sprint.mission.discodeit.entity.dto.mapper;

import com.sprint.mission.discodeit.entity.base.User;
import com.sprint.mission.discodeit.entity.dto.UserDTO;

final class UserMapper {
    private UserMapper() {}

    public static User toUser(UserDTO dto) {
        return User.fromDto(
                dto.getUuid(),
                dto.getCreatedAt(),
                dto.getUpdatedAt(),
                dto.getUserId(),
                dto.getPasswd(),
                dto.getDisplayName(),
                dto.getBio(),
                User.Status.valueOf(dto.getOnlineStatus())
        );
    }

    public static UserDTO toDto(User user) {
        return new UserDTO(
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

