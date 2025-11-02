package com.sprint.mission.discodeit.dto.channel.request;

import com.sprint.mission.discodeit.enums.ChannelScope;
import com.sprint.mission.discodeit.enums.ChannelType;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record PublicChannelCreateRequest(
        @NotNull(message = "채널이름은 필수입니다.")
        String channelName,
        @NotNull(message = "공개 범위는 필수입니다.")
        ChannelScope scope,
        @NotNull(message = "타입은 필수입니다.")
        ChannelType type,
        String description,
        @NotNull(message = "한 명 이상의 운영자는 필수입니다.")
        Set<String> moderatorIds,
        @NotNull(message = "한 명 이상의 멤버는 필수입니다.")
        Set<String> memberIds
) {
}
