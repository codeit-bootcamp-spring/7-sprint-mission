package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FileChannelRepository implements ChannelRepository {
    private final FileManager<Channel> fileManager;
    private final Map<UUID, Channel> channels;

    public FileChannelRepository(FileManager<Channel> fileManager) {
        this.fileManager = fileManager;
        this.channels = new HashMap<>();
    }

    @Override
    public void save(Channel channel) {
        channels.put(channel.getId(), channel);
        fileManager.writeFile(channels);
    }

    @Override
    public Optional<Channel> findById(UUID channelId) {
        return Optional.ofNullable(channels.get(channelId));
    }

    @Override
    public List<Channel> findAll() {
        return  new ArrayList<>(channels.values());
    }

    @Override
    public void deleteById(UUID channelId) {
        channels.remove(channelId);
        fileManager.writeFile(channels);
    }

    @Override
    public boolean existsById(UUID channelId) {
        return channels.containsKey(channelId);
    }
}
