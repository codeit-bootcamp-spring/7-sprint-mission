package com.sprint.mission.discodeit.dto.fileIo.mapper;

import com.sprint.mission.discodeit.dto.fileIo.BinaryContentIoDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;

final class BinaryContentMapper {
    private BinaryContentMapper() {
    }

    public static BinaryContentIoDTO toDto(BinaryContent content) {
        return new BinaryContentIoDTO(
                content.getId(),
                content.getUploadedAt(),
                content.getFileUrl()
        );
    }

    public static BinaryContent toBinaryContent(BinaryContentIoDTO dto) {
        return BinaryContent.fromDto(
                dto.getUuid(),
                dto.getUploadedAt(),
                dto.getFileUrl()
        );
    }
}

