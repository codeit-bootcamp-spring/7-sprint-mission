package com.sprint.mission.discodeit.dto.binarycontent.request;

public record CreateBinaryContentRequestDto(
        String fileName,
        Long size,
        String contentType,
        byte[] bytes
) { }
