package com.sprint.mssion.discodeit.repository.file;

import com.sprint.mssion.discodeit.entity.Channel;
import com.sprint.mssion.discodeit.entity.User;
import com.sprint.mssion.discodeit.repository.ChannelRepository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileChannelRepository implements ChannelRepository {
    private final Path filePath = Paths.get("src", "main", "resources", "Channels.ser");
    private final FileManager<Channel> fileManager;
    public FileChannelRepository() {
        fileManager = new FileManager<>(filePath);
    }

    @Override
    public void save(Channel channel) {
        Map<UUID, Channel> channels = fileManager.readFile();
        channels.put(channel.getCommon().getId(), channel);
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
