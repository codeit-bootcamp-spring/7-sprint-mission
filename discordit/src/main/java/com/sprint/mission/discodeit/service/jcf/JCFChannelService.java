package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.base.Channel;
import com.sprint.mission.discodeit.entity.base.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFChannelService extends BasicChannelService {

    public JCFChannelService(ChannelRepository channelRepository) {
        super(channelRepository);
    }
}
