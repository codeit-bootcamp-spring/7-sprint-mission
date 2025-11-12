package com.sprint.mission.discodeit.service.dto.request;

public record UserUpdateReq(
        String email,
        String username,
        String phoneNumber,
        String password
) {
}
