package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record ChannelPrivateInfoRes(
        List<UUID> users,                   //채널 참가자
        Instant lastMessageTime            //가장 최근 메세지의 시간정보
) implements ChannelInfoRes{
    public static ChannelPrivateInfoRes from(Channel channel, Message message) {
        return ChannelPrivateInfoRes.builder()
                .users(channel.getUsers())
                .lastMessageTime(message.getCreatedAt())
                .build();
    }
}
