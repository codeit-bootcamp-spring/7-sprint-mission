package com.sprint.mission.discodeit.dto.channel.response;

import com.sprint.mission.discodeit.entity.Channel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@SuperBuilder
@ToString
public class PrivateChannelResponseDto extends ChannelResponseDto {
    private List<UUID> memberIds;

    public static PrivateChannelResponseDto from(Channel channel, Instant lastedMessageAt) {
        return PrivateChannelResponseDto.builder()
                .id(channel.getId())
                .channelName(channel.getChannelName())
                .channelType(channel.getChannelType())
                .visibility(channel.getVisibility())
                .adminId(channel.getAdminId())
                .memberIds(channel.getMemberIds())
                .lastedMessageAt(lastedMessageAt)
                .build();
    }
}
