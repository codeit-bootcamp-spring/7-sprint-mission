package com.sprint.mission.discodeit.service.dto.request;

public record UserUpdateRequest(
        String newEmail,
        String newUsername,
        String newPassword
) {
}
