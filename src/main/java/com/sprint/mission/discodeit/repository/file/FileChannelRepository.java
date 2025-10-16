package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.Optional;
import java.util.UUID;

public class FileChannelRepository extends FileBaseRepository<Channel> implements ChannelRepository {

    public FileChannelRepository(String rootPath) {
        super(rootPath + "Data.ser");
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
