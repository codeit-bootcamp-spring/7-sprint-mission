package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.BaseInterfaceRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class FileChannelRepository implements BaseInterfaceRepository<Channel> {
    private final FileUtil fileUtil;

    public FileChannelRepository(@Qualifier("channelFileUtil") FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    @Override
    public void save(Channel channel) {
        fileUtil.saveRepository(channel);
    }

    @Override
    public boolean deleteById(UUID id) {
        return fileUtil.deleteRepository(id);
    }

    @Override
    public Optional<Channel> findById(UUID channelID) {
        Channel channel = (Channel) fileUtil.findModel(channelID).orElseThrow(() -> new IllegalArgumentException("🚨channel readStatusID = [" + channelID.toString()+ "] 오류"));
        return Optional.ofNullable(channel);
    }

    @Override
    public List<Channel> findAll() {
        return fileUtil.findAll().stream().map(model -> (Channel)model).toList();
    }
}
