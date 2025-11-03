package com.sprint.mission.discodeit.dto.channel.response;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ChannelVisibility;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Getter
@SuperBuilder
@ToString
public class ChannelResponseDto {
    private UUID id;
    private String channelName;
    private ChannelType channelType;
    private ChannelVisibility visibility;
    private UUID adminId;
    private Instant lastedMessageAt;

    public static ChannelResponseDto from(Channel channel, Instant lastedMessageAt) {
        return ChannelResponseDto.builder()
                .id(channel.getId())
                .channelName(channel.getChannelName())
                .channelType(channel.getChannelType())
                .visibility(channel.getVisibility())
                .adminId(channel.getAdminId())
                .lastedMessageAt(lastedMessageAt)
                .build();
    }
}
