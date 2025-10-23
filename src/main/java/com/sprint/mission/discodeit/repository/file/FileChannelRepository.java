package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FileChannelRepository implements ChannelRepository {
    private final FileManager<Channel> fileManager;
    public FileChannelRepository(FileManager<Channel> fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void save(Channel channel) {
        Map<UUID, Channel> channels = fileManager.readFile();
        channels.put(channel.getId(), channel);
        fileManager.writeFile(channels);
    }

    @Override
    public Optional<Channel> findById(UUID channelId) {
        Map<UUID, Channel> channels = fileManager.readFile();
        return Optional.ofNullable(channels.get(channelId));
    }

    @Override
    public List<Channel> findAll() {
        Map<UUID, Channel> channels = fileManager.readFile();
        return  new ArrayList<>(channels.values());
    }

    @Override
    public void deleteById(UUID channelId) {
        Map<UUID, Channel> channels = fileManager.readFile();
        channels.remove(channelId);
        fileManager.writeFile(channels);
    }

    @Override
    public boolean existsById(UUID channelId) {
        Map<UUID, Channel> channels = fileManager.readFile();
        return channels.containsKey(channelId);
    }
}
