package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

public class FileChannelRepository extends FileBaseRepository<Channel> implements ChannelRepository {

    private static final String CHANNEL_DATA_FILE = "channelData.ser";

    public FileChannelRepository(String basePath) {
        super(basePath + "/" + CHANNEL_DATA_FILE);
    }

}
