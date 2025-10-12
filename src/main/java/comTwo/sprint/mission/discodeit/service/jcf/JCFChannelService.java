package com2.sprint.mission.discodeit.service.jcf;

import com2.sprint.mission.discodeit.entity.Channel;
import com2.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

/**
 * ChannelService의 JCF(Java Collections Framework) 기반 구현체
 * 내부적으로 HashMap을 사용하여 Channel 데이터를 메모리에 저장한다.
 */
public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data; // Channel 데이터를 저장하는 Map

    public JCFChannelService() {
        this.data = new HashMap<>();
    }

    /**
     * Channel 생성 (등록)
     */
    @Override
    public Channel create(Channel channel) {
        data.put(channel.getId(), channel);
        return channel;
    }

    /**
     * Channel 단건 조회
     */
    @Override
    public Channel read(UUID id) {
        return data.get(id);
    }

    /**
     * 전체 Channel 조회
     */
    @Override
    public List<Channel> readAll() {
        return new ArrayList<>(data.values());
    }

    /**
     * Channel 수정
     */
    @Override
    public Channel update(UUID id, Channel channel) {
        if (data.containsKey(id)) {
            data.put(id, channel);
            return channel;
        }
        return null;
    }

    /**
     * Channel 삭제
     */
    @Override
    public boolean delete(UUID id) {
        return data.remove(id) != null;
    }
}