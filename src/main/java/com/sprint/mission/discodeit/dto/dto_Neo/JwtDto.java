package com.sprint.mission.discodeit.dto.dto_Neo;

import lombok.Builder;

@Builder
public record JwtDto(
    UserDto userDto,
    String accessToken
) {
}
