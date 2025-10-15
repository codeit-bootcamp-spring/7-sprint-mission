package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.UUID;

/**
 * ✅ FileChannelService
 *  - Channel 비즈니스 로직 담당
 *  - Repository를 통해 파일에 데이터 저장
 */
public class FileChannelService implements ChannelService {
    private final ChannelRepository repo;

    public FileChannelService() { this.repo = new FileChannelRepository(); }
    public FileChannelService(ChannelRepository repo) { this.repo = repo; }

    @Override
    public Channel create(String channelName) {
        if (channelName == null || channelName.isBlank()) throw new IllegalArgumentException("channelName 비어있음");
        return repo.save(new Channel(channelName));
    }

    @Override public Channel read(UUID id) { return repo.findById(id); }
    @Override public List<Channel> readAll() { return repo.findAll(); }

    @Override
    public Channel updateChannelName(UUID id, String newName) {
        Channel ch = repo.findById(id);
        if (ch != null && newName != null && !newName.isBlank()) { ch.updateChannelName(newName); repo.save(ch); }
        return ch;
    }

    @Override
    public Channel updateChannelTopic(UUID id, String newTopic) {
        Channel ch = repo.findById(id);
        if (ch != null) { ch.updateChannelTopic(newTopic); repo.save(ch); }
        return ch;
    }

    @Override public boolean delete(UUID id) { return repo.deleteById(id); }
}