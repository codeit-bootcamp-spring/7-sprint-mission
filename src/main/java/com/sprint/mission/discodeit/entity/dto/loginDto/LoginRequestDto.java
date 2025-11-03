package com.sprint.mission.discodeit.entity.dto.loginDto;

import lombok.Builder;
import lombok.NonNull;


@Builder
public record LoginRequestDto(@NonNull String email, @NonNull String password) {

}
