package com.sprint.mission.discodeit.dto.channel.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreatePrivateChannelDto(
        @NotNull(message = "참여자 목록은 필수입니다.")
        @NotEmpty(message = "참여자 목록이 비어있습니다.")
        List<UUID> participantsIds
) {
}
