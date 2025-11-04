package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class JCFChannelRepository implements ChannelRepository {
    //채널 데이터
    private final Map<UUID, Channel> data = new ConcurrentHashMap<>();

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
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    
    //채널 이름
    @Override
    public Optional<Channel> findByName(String name) {
        return findAll().stream()
                .filter(ch -> ch.getName().equals(name))
                .findFirst();
    }

    //수정
    @Override
    public void update(UUID id, String name, String description) {
        data.get(id).update(name, description);
    }

    //삭제
    @Override
    public void delete(UUID id) {
        data.remove(id);
    }

    //유저가 해당 채널에 포함되어 있는지 확인
    @Override
    public boolean isMember(UUID userId, UUID channelId) {
        return findAll().stream()
                .filter(ch -> ch.getId().equals(channelId))
                .anyMatch(ch -> ch.getUsers().contains(userId));
    }

    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }
}
