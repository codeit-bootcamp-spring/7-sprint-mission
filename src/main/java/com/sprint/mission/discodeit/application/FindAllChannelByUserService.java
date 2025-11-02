package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.domain.Server;
import com.sprint.mission.discodeit.domain.repository.ChannelRepository;
import com.sprint.mission.discodeit.domain.repository.ServerRepository;
import com.sprint.mission.discodeit.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FindAllChannelByUserService {

    private final ServerRepository serverRepository;
    private final ChannelRepository channelRepository;

    public List<UUID> findAllChannelByUser(UUID serverId, UUID userId){
        Server server = ServerFindHelper.findById(serverRepository, serverId);
        List<UUID> list = server.getChannels().stream()
                .map(channelId -> ChannelFindHelper.findById(channelRepository, channelId))
                .filter(channel -> channel.getChannelMember().contains(userId))
                .map(channel -> channel.getId())
                .toList();
        return list;

    }

}
