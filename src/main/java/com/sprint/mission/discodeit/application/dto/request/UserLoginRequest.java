package com.sprint.mission.discodeit.application.dto.request;

public record UserLoginRequest(
        String username,
        String password
) {
}
