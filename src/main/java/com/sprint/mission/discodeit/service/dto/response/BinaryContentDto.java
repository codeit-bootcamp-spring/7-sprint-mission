package com.sprint.mission.discodeit.service.dto.response;

import com.sprint.mission.discodeit.domain.BinaryContent;

public record BinaryContentDto(
        String id,
        String fileName,
        long size,
        String contentType


) {
    public static BinaryContentDto from(BinaryContent content){
        return new BinaryContentDto(content.getId(),
                content.getFileName(),
                content.getFileSize(),
                content.getFileType());
    }
}
