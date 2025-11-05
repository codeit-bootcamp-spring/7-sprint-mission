package com.sprint.mission.discodeit.dto.fileIo.mapper;

import com.sprint.mission.discodeit.dto.fileIo.BinaryContentIoDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exceptions.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;

final class BinaryContentMapper {
    private BinaryContentMapper() {
    }

    public static BinaryContentIoDTO toDto(BinaryContent content) {
        return new BinaryContentIoDTO(
                content.getId(),
                content.getUploadedAt(),
                content.getUploadUser().getUuid(),
                content.getFileUrl()
        );
    }

    public static BinaryContent toBinaryContent(BinaryContentIoDTO dto, UserRepository userRepository) {
        User uploader = userRepository.findById(dto.getUploadUserUuid())
                .orElseThrow(() -> new UserNotFoundException(dto.getUploadUserUuid()));
        return BinaryContent.fromDto(
                dto.getUuid(),
                dto.getUploadedAt(),
                uploader,
                dto.getFileUrl()
        );
    }
}

