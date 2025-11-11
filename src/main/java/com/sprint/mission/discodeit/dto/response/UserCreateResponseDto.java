package com.sprint.mission.discodeit.dto.response;

import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Builder;

import javax.print.attribute.standard.JobOriginatingUserName;
import java.time.Instant;
import java.util.UUID;

@Builder
public record UserCreateResponseDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String username,
        String email,
        String password,
        UUID profileId
) {
}
