package com.sprint.mission.discodeit.application.dto.response;

public record ServerResponseDto(
        String servername,
        Long serverLevel,
        boolean isPrivate
) {
}
