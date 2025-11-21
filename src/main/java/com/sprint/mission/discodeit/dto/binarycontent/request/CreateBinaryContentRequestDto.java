package com.sprint.mission.discodeit.dto.binarycontent.request;

public record CreateBinaryContentRequestDto(
        String fileName,
        String contentType,
        byte[] bytes
) { }
