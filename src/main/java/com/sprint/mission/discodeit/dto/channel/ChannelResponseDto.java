package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Common;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.enums.ChannelType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@ToString
public class ChannelResponseDto {
    private UUID channelId;
    private ChannelType channelType;
    private String channelName;
    private String desc;
    private List<User> participants;
    private Instant lastMessageAt;

    public static ChannelResponseDto from(Channel channel, Instant lastMessageAt) {
        List<UUID> participantIds = null;


        if (channel.getChannelType() == ChannelType.PRIVATE) {
            participantIds = channel.getParticipants().stream()
                    .map(Common::getId)
                    .toList();
        }

        return ChannelResponseDto.builder()
                .channelId(channel.getId())
                .channelName(channel.getChannelName())
                .desc(channel.getDesc())
                .participants(channel.getParticipants())
                .channelType(channel.getChannelType())
                .lastMessageAt(lastMessageAt)
                .build();
    }

}

