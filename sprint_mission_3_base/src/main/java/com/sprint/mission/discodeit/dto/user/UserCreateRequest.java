package com.sprint.mission.discodeit.dto.user;

public record UserCreateRequest(
        String username,
        String email,
        String password,
        ProfileImageCreateRequest profileImage // null 가능(프로필 미지정)
) {}
