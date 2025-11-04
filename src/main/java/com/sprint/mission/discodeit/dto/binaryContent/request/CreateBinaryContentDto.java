package com.sprint.mission.discodeit.dto.binaryContent.request;

public record CreateBinaryContentDto(
        String fileName,
        String contentType,
        byte[] bytes
) {
}
