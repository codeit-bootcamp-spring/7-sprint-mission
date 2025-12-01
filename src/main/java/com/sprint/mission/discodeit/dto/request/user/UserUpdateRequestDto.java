package com.sprint.mission.discodeit.dto.request.user;

public record UserUpdateRequestDto(
        String newUsername,
        String newEmail,
        String newPassword) {
}
