package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.*;
import java.util.*;

public interface ChannelService {
    ChannelDto createPublic(CreatePublicChannelRequest request);
    ChannelDto createPrivate(CreatePrivateChannelRequest request);
    ChannelDto find(UUID channelId);
    List<ChannelDto> findAllByUserId(UUID userId);
    ChannelDto update(ChannelUpdateRequest request);
    void delete(UUID channelId);
    
}
