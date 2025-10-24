package com.sprint.mission.discodeit.server.presentation.dto.response;

public record ServerResponseDto(
        String servername,
        Long serverLevel,
        boolean isPrivate
) {
}
