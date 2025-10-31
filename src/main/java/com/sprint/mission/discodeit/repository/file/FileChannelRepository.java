package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.List;
import java.util.UUID;

public class FileChannelRepository extends BaseFileRepository<Channel> implements ChannelRepository {
    public FileChannelRepository() {
        super(Channel.class);
    }

    //저장
    @Override
    public Channel save(Channel channel) {
        saveToFile(channel.getId(), channel);
        return channel;
    }

    //채널 모두 찾기
    @Override
    public List<Channel> findAll() {
        return findAllFiles();
    }
    
    //채널 id로 조회
    @Override
    public Channel findById(UUID id) {
        return loadFromFile(id);
    }

    //채널 이름으로 조회
    @Override
    public Channel findByName(String name) {
        return findAllFiles().stream()
                .filter(channel -> channel.getName().equals(name))
                .findFirst().orElse(null);
    }

    //채널명 수정
    @Override
    public Channel update(UUID id, String name, String description) {
        Channel channel = loadFromFile(id);
        if (channel == null) {
            throw new RuntimeException("Failed to update channel with id=" + id);
        }
        channel.update(name, description);
        saveToFile(channel.getId(), channel);
        return channel;
    }

    //채널 삭제
    @Override
    public Channel delete(UUID id) {
        Channel channel = loadFromFile(id);
        deleteFile(id);
        return channel;
    }

    //유저가 해당 채널에 포함되어 있는지 확인
    @Override
    public boolean isMember(UUID userId, UUID channelId) {
        return findAllFiles().stream()
                .filter(ch -> ch.getId().equals(channelId))
                .anyMatch(ch -> ch.getUsers().contains(userId));
    }
}
