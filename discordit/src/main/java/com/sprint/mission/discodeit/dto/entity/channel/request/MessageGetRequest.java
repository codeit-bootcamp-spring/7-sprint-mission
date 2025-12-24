package com.sprint.mission.discodeit.dto.entity.channel.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public record MessageGetRequest(
        @NotNull(message = "아이디는 필수입니다.")
        UUID channelId,

        Pageable pageable
) {
}
