package com.sprint.mission.discodeit.dto.channel.response;

import com.sprint.mission.discodeit.entity.Channel;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record ChannelPrivateInfoRes(
        String type,
        List<UUID> users,                   //채널 참가자
        Instant lastMessageTime            //가장 최근 메세지의 시간정보
) implements ChannelInfoRes{
    public static ChannelPrivateInfoRes from(Channel channel, Instant lastMessageTime) {
        return ChannelPrivateInfoRes.builder()
                .type(channel.getPublicType().getValue())
                .users(channel.getUsers())
                .lastMessageTime(lastMessageTime)
                .build();
    }
}
