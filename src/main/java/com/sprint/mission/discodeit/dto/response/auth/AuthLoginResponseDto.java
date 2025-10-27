package com.sprint.mission.discodeit.dto.response.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class AuthLoginResponseDto {
    private final UUID userId;
    private final String username;
}
