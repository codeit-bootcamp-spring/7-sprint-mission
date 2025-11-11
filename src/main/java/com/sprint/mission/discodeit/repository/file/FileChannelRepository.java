package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.enum_.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.File;
import java.util.*;

public class FileChannelRepository extends AbstractFileRepository<Channel, UUID> implements ChannelRepository {


    private final String filePath;

    public FileChannelRepository(String fileDirectory) {
        this.filePath = fileDirectory + File.separator + "channels.ser";
    }

    @Override
    protected String getFilePath() {
        return filePath;
    }

    @Override
    protected UUID getId(Channel channel) {
        return channel.getId();
    }

    @Override
    public Optional<Channel> findByName(String channelName) {
        return findAll().stream()
                .filter(channel -> channel.getChannelName().equals(channelName))
                .findFirst();
    }
    @Override
    public List<Channel> findChannelsByUserId(UUID userId) {
        return findAll().stream()
                .filter(channel ->
                        channel.getType() == ChannelType.PUBLIC ||
                                (channel.getType() == ChannelType.PRIVATE && channel.getMembers().contains(userId)))
                .toList();
    }
}
