package com.sprint.mission.discodeit.dto.fileIo.mapper;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.dto.fileIo.UserIoDTO;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

final class UserMapper {
    private UserMapper() {
    }

    public static User toUser(UserIoDTO dto, BinaryContentRepository contentRepository) {
        return User.fromDto(
                dto.getUuid(),
                dto.getCreatedAt(),
                dto.getUpdatedAt(),
                dto.getUserId(),
                dto.getPasswd(),
                dto.getEmail(),
                dto.getDisplayName(),
                dto.getBio(),
                dto.getOnlineStatus(),
                dto.getProfileImageId() == null? null : contentRepository.findById(dto.getProfileImageId())
        );
    }

    public static UserIoDTO toDto(User user) {
        return new UserIoDTO(
                user.getUuid(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getUserId(),
                user.getPasswd(),
                user.getEmail(),
                user.getDisplayName(),
                user.getBio(),
                user.getOnlineStatus(),
                user.getProfileImage() == null ? null : user.getProfileImage().getId()
        );
    }
}
