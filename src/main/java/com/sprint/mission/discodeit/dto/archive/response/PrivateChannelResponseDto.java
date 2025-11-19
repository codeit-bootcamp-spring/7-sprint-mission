package com.sprint.mission.discodeit.dto.archive.response;

import com.sprint.mission.discodeit.entity.Channel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ToString
public class PrivateChannelResponseDto extends ChannelResponseDto {
    List<UUID> memberIds;

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
