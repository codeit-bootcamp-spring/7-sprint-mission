package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Map;
import java.util.UUID;

// 생성 , 읽기 , 모두읽기, 수정, 삭제
public interface ChannelService {
    Channel create(Channel channel);
    Channel get(UUID id);
    Channel update(Channel channel);
    boolean delete(UUID id);
    List<Channel> getAll();
    boolean join(UUID channelId, UUID userId);
    boolean leave(UUID channelId, UUID userId);
    void setSlowModeSeconds(UUID channelId, int slowModeSeconds);
}
