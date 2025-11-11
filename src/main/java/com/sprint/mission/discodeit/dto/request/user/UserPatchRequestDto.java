package com.sprint.mission.discodeit.dto.request.user;

import lombok.Builder;

@Builder
public record UserPatchRequestDto(
        UserUpdateRequest userUpdateRequest,
        byte[] profile
) {
}
