package com.sprint.mission.discodeit.entity.dto.channelDto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.entity.dto.ChangeTime.changeTime;

@Builder
public record ChannelInfoDto(UUID id,
                             String channelName,
                             String channelAdmin,
                             String channelType,    // public, private
                             Instant createdAt,     // 채널 생성일자
                             Instant lastMessageAt, // 마지막 메시지 시간
                             List<UUID> memberIds   // Private 전용
) {

    public static ChannelInfoDto from(Channel channel, Message lastMessage) {

        List<UUID> ids = null;
        if (channel.getType() == ChannelType.PRIVATE) {
            ids = channel.getMembers().stream().map(User::getId).toList();
        }
        Instant last = lastMessage != null ? lastMessage.getCreatedAt() : null;

        return ChannelInfoDto.builder()
                .id(channel.getId())
                .channelName(channel.getChannelName())
                .channelAdmin(channel.getChannelAdmin().getUserName())
                .channelType(channel.getType().toString())
                .createdAt(channel.getCreatedAt())
                .lastMessageAt(last)
                .memberIds(ids)
                .build();
    }

    public String toString() {

        String lastMessage = lastMessageAt != null ? ", 마지막 메시지" + lastMessageAt : "";

        return channelName + '[' +
                "관리자: " + channelAdmin +
                ", 채널유형: " + channelType +
                ", 생성일자: " + createdAt +
                lastMessage +
                "]";
    }
}
