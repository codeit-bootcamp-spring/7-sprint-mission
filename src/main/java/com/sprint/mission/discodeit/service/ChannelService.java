package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

// 생성 , 읽기 , 모두읽기, 수정, 삭제
public interface ChannelService {
    Channel createPublic(PublicChannelCreateRequestDto publicChannelCreateRequestDto);
    Channel createPrivate(PrivateChannelCreateRequestDto privateChannelCreateRequestDto);
    Channel get(UUID channelId);
    List<Channel> getAllByUserId(UUID userId);
    Channel update(ChannelUpdateRequestDto channelUpdateRequestDto);
    boolean delete(UUID channelId);
    List<Channel> getAll();
    boolean join(UUID channelId, UUID userId);
    boolean leave(UUID channelId, UUID userId);
    void setSlowModeSeconds(UUID channelId, int slowModeSeconds);
    Instant getLastMessageAt(UUID channelId);
}
