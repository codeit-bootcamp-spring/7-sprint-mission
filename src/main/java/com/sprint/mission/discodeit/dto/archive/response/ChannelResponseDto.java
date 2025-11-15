package com.sprint.mission.discodeit.dto.archive.response;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ChannelVisibility;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Getter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ToString
public class ChannelResponseDto {
    UUID id;
    String channelName;
    ChannelType channelType;
    ChannelVisibility visibility;
    UUID adminId;
    Instant lastedMessageAt;

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
