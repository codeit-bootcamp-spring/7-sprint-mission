package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

public record ProfileImageDto(
        UUID id,
        String filename,
        String contentType
) {}
