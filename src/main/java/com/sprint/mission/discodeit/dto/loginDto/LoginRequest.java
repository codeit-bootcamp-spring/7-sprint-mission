package com.sprint.mission.discodeit.dto.loginDto;

import lombok.Builder;
import lombok.NonNull;


@Builder
public record LoginRequest(@NonNull String username, @NonNull String password) {

}
