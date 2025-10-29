package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.List;
import java.util.UUID;

public class FileChannelRepository extends BaseFileRepository<Channel> implements ChannelRepository {
    //싱글톤 구현
    private final static FileChannelRepository instance = new FileChannelRepository();

    private FileChannelRepository() {
        super(Channel.class);
    }

    public static FileChannelRepository getInstance() {
        return instance;
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
    public Channel update(UUID id, String name) {
        Channel channel = loadFromFile(id);
        if (channel == null) {
            throw new RuntimeException("Failed to update channel with id=" + id);
        }
        channel.update(name);
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
}
