package com.sprint.mission.discodeit.dto.channelDto;

import com.sprint.mission.discodeit.dto.userDto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.entityType.ChannelType;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record ChannelDto(UUID id,
                         ChannelType type,    // public, private
                         String name,
                         String description,
                         List<UserDto> participants,   // Private 전용
                         Instant lastMessageAt // 마지막 메시지 시간
) {

    public static ChannelDto from(
            Channel channel, List<UserDto> participants,
            Message lastMessage) {

        Instant last = lastMessage != null ? lastMessage.getCreatedAt() : null;

        return new ChannelDto(
                channel.getId(),
                channel.getType(),
                channel.getName(),
                channel.getDescription(),
                participants,
                last
        );
    }
}
