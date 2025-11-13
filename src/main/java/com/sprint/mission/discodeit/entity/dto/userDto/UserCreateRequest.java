package com.sprint.mission.discodeit.entity.dto.userDto;

import lombok.*;

@Builder
public record UserCreateRequest(
        @NonNull
        String email,
        @NonNull
        String userName,
        @NonNull
        String password) {
}
