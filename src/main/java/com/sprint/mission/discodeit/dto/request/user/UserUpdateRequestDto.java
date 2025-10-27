package com.sprint.mission.discodeit.dto.request.user;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class UserUpdateRequestDto {
    @NotNull(message = "id는 필수입니다!")
    private final UUID id;
    private final String username;
    private final String email;
    private final String password;
    private final String profileFileName;
    private final String profileContentType;
    private final byte[] profileData;
}
