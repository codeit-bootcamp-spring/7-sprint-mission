package com.sprint.mission.discodeit.dto.channel.request;

import jakarta.validation.constraints.NotNull;

public record GetVisibleChannelRequest(
        @NotNull(message = "id는 필수입니다.")
        String userId
) {
}
