package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

public record ChannelCreateRequest(
        @NotNull(message = "채널 타입은 필수입니다.")
        ChannelType type,

        @NotBlank(message = "채널 이름은 필수입니다.")
        @Size(max = 100, message = "채널 이름은 100자 이하여야 합니다.")
        String name,

        List<UUID> participantIds
) {
}
