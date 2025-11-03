package com.sprint.mission.discodeit.dto.message;

public record AttachmentCreateRequest(
        String filename,
        String contentType,
        byte[] data
) {}
