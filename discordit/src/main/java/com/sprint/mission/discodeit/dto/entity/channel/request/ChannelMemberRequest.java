package com.sprint.mission.discodeit.dto.entity.channel.request;


import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record ChannelMemberRequest(
        @NotNull(message = "채널 아이디는 필수입니다.")
        UUID channelId,
        @NotNull(message = "가입 유저 id는 필수입니다.")
        List<UUID> userUuids
) {
}
