package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChannelCreateRequest(
        @NotBlank(message = "채널 이름은 필수입니다.")
        @Size(max = 100, message = "채널 이름은 100자 이하여야 합니다.")
        String name
) {
}
