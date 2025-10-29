package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.application.dto.ChannelDtoMapper;
import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.repository.ChannelRepository;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public final class ChannelFindHelper {
    public static Channel findById(ChannelRepository repo, UUID channelId){
        return repo.findById(channelId).orElseThrow(()->new NoSuchElementException("해당 파일이 없습니다"));
    }

    public static List<Channel> findAll(ChannelRepository repo, UUID channelId){
        return repo.findAll();
    }

    public static List<Channel> findByServer(ChannelRepository repo, UUID serverId){
        return repo.findAll()
                .stream()
                .filter(c-> c.getServerId().equals(serverId))
                .toList();
    }
}
