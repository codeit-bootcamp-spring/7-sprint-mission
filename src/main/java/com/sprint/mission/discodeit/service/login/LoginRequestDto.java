package com.sprint.mission.discodeit.service.login;

import lombok.Builder;
import lombok.NonNull;


@Builder
public record LoginRequestDto(@NonNull String email, @NonNull String password) {

}
