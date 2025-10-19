package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.UUID;

public class FileChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    public FileChannelService(String filePath) {
        this.channelRepository = new FileChannelRepository(filePath);
    }

    @Override
    public Channel create(String name) {
        Channel c = new Channel(name);
        channelRepository.save(c);
        return c;
    }

    @Override
    public Channel findById(UUID id) {
        return channelRepository.findById(id);
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public void update(UUID id, String name) {
        Channel c = channelRepository.findById(id);
        if (c != null) {
            c.update(name);
            channelRepository.save(c);
        }
    }

    @Override
    public void delete(UUID id) {
        channelRepository.delete(id);
    }
}