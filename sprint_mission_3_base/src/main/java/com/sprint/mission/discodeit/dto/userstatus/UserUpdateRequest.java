package com.sprint.mission.discodeit.dto.userstatus;

import com.sprint.mission.discodeit.dto.user.ProfileImageCreateRequest;

import java.util.UUID;

public record UserUpdateRequest(
        UUID id,
        String username,
        String email,
        String password,
        ProfileImageCreateRequest profileImage // null이면 기존 유지
) {}
