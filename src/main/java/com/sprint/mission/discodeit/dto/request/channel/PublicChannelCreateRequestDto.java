package com.sprint.mission.discodeit.dto.request.channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import jakarta.validation.constraints.NotBlank;

/**
 * @param description     0: 음성채널, 1: 채팅채널
 * @param slowModeSeconds 슬로우모드 초(s)
 */
public record PublicChannelCreateRequestDto(
        @NotBlank
        String name,
        String description,
        Integer slowModeSeconds,
        ChannelType type) {
}
