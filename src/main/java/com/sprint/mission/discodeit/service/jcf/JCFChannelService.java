package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.MemoryChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.UUID;

public class JCFChannelService implements ChannelService {



    private final MemoryChannelRepository channelRepository;


    public JCFChannelService(MemoryChannelRepository channelRepository){
        this.channelRepository=channelRepository;
    }

    @Override
    public void addChannel(Channel channel) {
        channelRepository.save(channel);
    }


    @Override
    public void removeChannel(Channel channel) {
        channelRepository.remove(channel);
    }

    @Override
    public Channel getChannel(UUID id) {
        return channelRepository.findById(id);
    }

    @Override
    public void updateChannel(UUID channelId, UpdatedChannelDTO updatedChannelDTO) {
        channelRepository.updateChannel(channelId,updatedChannelDTO);
    }

    public Channel openChannel(User user ,String serverName, Long serverLevel, boolean isPrivate){
        Channel channel = new Channel();
        channel.setManager(user);
        channel.setServerName(serverName);
        channel.setServerLevel(serverLevel);
        channel.setPrivate(isPrivate);
        ChannelUser channelUser = new ChannelUser(user);
        channelUser.setRole(Role.ADMIN);
        channel.getMembers().add(channelUser);
        user.getMyChannels().add(channel);

        channelRepository.save(channel);
        return channel;

    }
}
