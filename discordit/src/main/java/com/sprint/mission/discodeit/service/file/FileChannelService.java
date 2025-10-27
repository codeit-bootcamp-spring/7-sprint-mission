package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.entity.base.Channel;
import com.sprint.mission.discodeit.entity.base.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;

import java.util.*;
import java.util.stream.Collectors;

public class FileChannelService extends BasicChannelService {
    public FileChannelService(ChannelRepository channelRepository) {
        super(channelRepository);
    }
}
