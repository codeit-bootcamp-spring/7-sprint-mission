package com.sprint.mission.discodeit.mapper.dto;

import lombok.Builder;

@Builder
public record JwtDto(
    UserDto userDto,
    String accessToken
) {
}
