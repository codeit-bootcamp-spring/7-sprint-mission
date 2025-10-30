package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.UUID;

public record BinaryContentResponseDto (
        UUID binaryContentId,
        byte[] data,//표준 데이터
        String fileName,
        String fileType
) {
    public static BinaryContentResponseDto from(BinaryContent binaryContent) {
        return new BinaryContentResponseDto(
                binaryContent.getId(),
                binaryContent.getData(),
                binaryContent.getFileName(),
                binaryContent.getFileType()
        );
    }
}
