package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFChannelRepository implements ChannelRepository {
    //채널 데이터
    private final Map<UUID, Channel> data = new HashMap<>();

    //저장
    @Override
    public Channel save(Channel channel) {
        data.put(channel.getId(), channel);
        return channel;
    }
    
    //채널 목록
    @Override
    public List<Channel> findAll() {
        return data.values().stream().toList();
    }

    //채널 목록 중에 한 유저에게 해당하는 채널 목록
    @Override
    public List<Channel> findAllByUserId(UUID userId) {
        return findAll().stream()
                .filter(ch -> ch.getUsers().contains(userId))
                .toList();
    }

    //채널 ID 조회
    @Override
    public Channel findById(UUID id) {
        return data.get(id);
    }

    
    //채널 이름
    @Override
    public Channel findByName(String name) {
        return findAll().stream()
                .filter(ch -> ch.getName().equals(name))
                .findFirst().orElse(null);
    }

    //수정
    @Override
    public Channel update(UUID id, String name, String description) {
        return findById(id).update(name, description);
    }

    //삭제
    @Override
    public Channel delete(UUID id) {
        return data.remove(id);
    }

    //유저가 해당 채널에 포함되어 있는지 확인
    @Override
    public boolean isMember(UUID userId, UUID channelId) {
        return findAll().stream()
                .filter(ch -> ch.getId().equals(channelId))
                .anyMatch(ch -> ch.getUsers().contains(userId));
    }
}
