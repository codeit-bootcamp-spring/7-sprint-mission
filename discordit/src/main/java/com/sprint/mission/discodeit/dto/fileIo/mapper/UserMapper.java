package com.sprint.mission.discodeit.dto.fileIo.mapper;

import com.sprint.mission.discodeit.entity.base.User;
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
                contentRepository.findById(dto.getProfileImageId())
        );
    }

    // TODO: BinaryContentRepository 미주입 환경에서 사용자 프로필 이미지는 복원하지 않음(차후 구현)
    public static User toUser(UserIoDTO dto) {
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
                null
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
