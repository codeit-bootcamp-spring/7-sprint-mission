package com.sprint.mission.discodeit.security.access;

import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("channelSecurity")
@RequiredArgsConstructor
public class ChannelSecurity {

    private final ChannelRepository channelRepository;

    public boolean isPrivate(UUID channelId) {
        return channelRepository.findById(channelId)
                .map(channel -> channel.getChannelType() == ChannelType.PRIVATE)
                .orElse(false);
    }
}
