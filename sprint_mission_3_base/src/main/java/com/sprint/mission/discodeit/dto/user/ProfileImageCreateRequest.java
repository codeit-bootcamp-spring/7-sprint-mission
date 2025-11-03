package com.sprint.mission.discodeit.dto.user;

public record ProfileImageCreateRequest(
        String filename,
        String contentType,
        byte[] data
) {}
