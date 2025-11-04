package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.List;
import java.util.Optional;
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

    //채널 중에 한 유저에게 해당하는 채널 목록
    @Override
    public List<Channel> findAllByUserId(UUID userId) {
        return findAllFiles().stream()
                .filter(ch -> ch.getUsers().contains(userId))
                .toList();
    }
    
    //채널 id로 조회
    @Override
    public Optional<Channel> findById(UUID id) {
        return loadFromFile(id);
    }

    //채널 이름으로 조회
    @Override
    public Optional<Channel> findByName(String name) {
        return findAllFiles().stream()
                .filter(channel -> channel.getName().equals(name))
                .findFirst();
    }

    //채널명 수정
    @Override
    public void update(UUID id, String name, String description) {
        loadFromFile(id).ifPresent(channel -> {
            channel.update(name, description);
            saveToFile(id, channel);
        });
    }

    //채널 삭제
    @Override
    public void delete(UUID id) {
        deleteFile(id);
    }

    //유저가 해당 채널에 포함되어 있는지 확인
    @Override
    public boolean isMember(UUID userId, UUID channelId) {
        return findAllFiles().stream()
                .filter(ch -> ch.getId().equals(channelId))
                .anyMatch(ch -> ch.getUsers().contains(userId));
    }

    @Override
    public boolean existsById(UUID id) {
        return fileExistsById(id);
    }
}
