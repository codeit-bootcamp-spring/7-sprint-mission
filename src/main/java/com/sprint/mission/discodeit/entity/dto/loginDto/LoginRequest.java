package com.sprint.mission.discodeit.entity.dto.loginDto;

import lombok.Builder;
import lombok.NonNull;


@Builder
public record LoginRequest(@NonNull String username, @NonNull String password) {

}
