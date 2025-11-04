package com.sprint.mission.discodeit.dto.user.request;

public record UpdateUserDto(
        String username,
        String password,
        String email,
        String phoneNumber,
        String pronoun
) {
}
