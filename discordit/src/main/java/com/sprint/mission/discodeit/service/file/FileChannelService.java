package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;

public class FileChannelService extends BasicChannelService {
    public FileChannelService(ChannelRepository channelRepository) {
        super(channelRepository);
    }
}
