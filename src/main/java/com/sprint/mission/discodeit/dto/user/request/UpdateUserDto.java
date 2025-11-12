package com.sprint.mission.discodeit.dto.user.request;

public record UpdateUserDto(
    String newUsername,
    String newEmail,
    String newPassword
) {

}
