package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.UpdatedChannelDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MemoryChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

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
        channel.getMembers().add(user);
        user.getMyChannels().add(channel);

        channelRepository.save(channel);
        return channel;

    }

    public void inviteMember(User fromUser ,Channel channel, User toUser) {
        if (!fromUser.getMyChannels().contains(channel)) {
            System.out.println("서버에 있지 않습니다.");
            return;
        }
        if (!(channel.getManager() == fromUser)) {
            System.out.println("초대 권한이 없습니다.");
            return;
        }
        channel.getMembers().add(toUser);

    }



}
