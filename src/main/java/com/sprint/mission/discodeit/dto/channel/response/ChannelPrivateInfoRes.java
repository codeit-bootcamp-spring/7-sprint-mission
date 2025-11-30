package com.sprint.mission.discodeit.dto.channel.response;

import com.sprint.mission.discodeit.entity.Channel;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelPrivateInfoRes(
    UUID channelId,
    String type,
    String channelName,
    UUID managerId,
    List<UUID> userIds,                   //채널 참가자
    Instant lastMessageTime            //가장 최근 메세지의 시간정보
) implements ChannelInfoRes {

}
