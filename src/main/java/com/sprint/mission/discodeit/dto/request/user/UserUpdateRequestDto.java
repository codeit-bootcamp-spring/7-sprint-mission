package com.sprint.mission.discodeit.dto.request.user;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserUpdateRequestDto(
        @NotNull(message = "id는 필수입니다!")
        UUID id,
        String username,
        String email,
        String password,
        String profileFileName,
        String profileContentType,
        byte[] profileData) {
}
