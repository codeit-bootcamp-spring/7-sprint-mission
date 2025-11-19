package com.sprint.mission.discodeit.dto.channel.response;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelUpdateResponse(
        UUID channelId,
        String newChannelName,
        String newDescription,
        String update
) {
    public static ChannelUpdateResponse from(Channel channel) {
        String status = (channel.getType() == ChannelType.PUBLIC)
                ? "업데이트적용"
                : "미적용";

        return new ChannelUpdateResponse(
                channel.getId(),
                channel.getName(),
                channel.getDescription(),
                status
        );
    }
}
