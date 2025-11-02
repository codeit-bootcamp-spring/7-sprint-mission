package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.global.util.file.FileManager;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.util.*;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileChannelRepository implements ChannelRepository {
    private final Path filePath;
    private final Map<UUID, Channel> channels;

    public FileChannelRepository(@Value("${file.path.channelPath}") Path channelFilePath) {
        this.filePath = channelFilePath;
        FileManager.init(filePath);
        this.channels = FileManager.readFile(filePath);
    }

    @Override
    public void save(Channel channel) {
        channels.put(channel.getId(), channel);
        FileManager.writeFile(filePath, channels);
    }

    @Override
    public Optional<Channel> findById(UUID channelId) {
        return Optional.ofNullable(channels.get(channelId));
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channels.values());
    }

    @Override
    public void deleteById(UUID channelId) {
        channels.remove(channelId);
        FileManager.writeFile(filePath, channels);
    }

    @Override
    public boolean existsById(UUID channelId) {
        return channels.containsKey(channelId);
    }
}
