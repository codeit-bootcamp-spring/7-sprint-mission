package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

// 생성 , 읽기 , 모두읽기, 수정, 삭제
public interface ChannelService {
    ChannelResponseDto createPublic(PublicChannelCreateRequestDto publicChannelCreateRequestDto);
    ChannelResponseDto createPrivate(PrivateChannelCreateRequestDto privateChannelCreateRequestDto);
    ChannelResponseDto get(UUID channelId);
    List<ChannelResponseDto> getAllByUserId(UUID userId);
    ChannelResponseDto update(ChannelUpdateRequestDto channelUpdateRequestDto);
    boolean delete(UUID channelId);
    List<ChannelResponseDto> getAll();
    boolean join(UUID channelId, UUID userId);
    boolean leave(UUID channelId, UUID userId);
    void setSlowModeSeconds(UUID channelId, int slowModeSeconds);
}
