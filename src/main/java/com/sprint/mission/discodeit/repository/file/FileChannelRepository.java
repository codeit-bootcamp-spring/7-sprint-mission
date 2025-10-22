package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class FileChannelRepository extends FileBaseRepository<Channel> implements ChannelRepository {

    private static final String CHANNEL_DATA_FILE = "channelData.ser";

    public FileChannelRepository() {
        super(CHANNEL_DATA_FILE);
    }

    @Override
    public boolean existsByAdminId(UUID adminId) {
        return data.values().stream()
                .anyMatch(c -> c.getChannelAdmin().getId().equals(adminId));
    }

    @Override
    public Optional<Channel> findByChannelName(String channelName) {
        return data.values().stream()
                .filter(c -> c.getChannelName().equals(channelName))
                .findFirst();
    }
}
