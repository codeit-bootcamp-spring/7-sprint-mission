package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.*;

public class FileChannelRepository extends AbstractFileRepository<Channel, UUID> implements ChannelRepository {

    private static final String CHANNEL_FILE_PATH = "channels.ser";

    @Override
    protected String getFilePath() {
        return CHANNEL_FILE_PATH;
    }

    @Override
    protected UUID getId(Channel channel) {
        return channel.getId();
    }

    @Override
    public Channel findByName(String channelName) {
        return entityMap.values()
                .stream()
                .filter(channel -> channel.getChannelName().equals(channelName))
                .findFirst()
                .orElse(null);
    }

}
