package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.InterfaceChannelRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FileChannelRepository implements InterfaceChannelRepository {
    private final FileUtil fileUtil;

    public FileChannelRepository(@Qualifier("channelFileUtil") FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    @Override
    public void save(Channel channel) {
        fileUtil.saveRepository(channel);
    }

    @Override
    public void deleteById(UUID id) {
        fileUtil.deleteRepository(id);
    }

    @Override
    public Optional<Channel> findById(UUID channelID) {
        Channel channel = (Channel) fileUtil.findModel(channelID).orElseThrow(() -> new IllegalArgumentException("🚨channel id = [" + channelID.toString()+ "] 오류"));
        return Optional.ofNullable(channel);
    }

    @Override
    public Optional<List<Channel>> findAll() {
        List<Channel> channels = fileUtil.findAll().stream().map(model -> (Channel)model).toList();
        return Optional.of(channels);
    }

    @Override
    public boolean existsById(UUID channelID) {
        return fileUtil.existsRepository(channelID);
    }

    @Override
    public boolean existsByName(String name) {
        return fileUtil.findAll().stream().map(channel -> (Channel)channel).anyMatch(channel -> channel.getChannelName().equals(name));
    }
}
