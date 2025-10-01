package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelInviteRequest;
import com.sprint.mission.discodeit.entity.ChannelUser;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MemoryChannelInviteRepository;

import java.util.List;
import java.util.UUID;

public class ChannelInviteRequestService {

    private final MemoryChannelInviteRepository channelInviteRepository;
    private final JCFChannelService channelService;

    public ChannelInviteRequestService(MemoryChannelInviteRepository channelInviteRepository,JCFChannelService channelService) {
        this.channelInviteRepository=channelInviteRepository;
        this.channelService=channelService;
    }
    public void sendChannelInviteRequest(User fromUser , Channel channel, User toUser) {
        if (!fromUser.getMyChannels().contains(channel)) {
            System.out.println("서버에 있지 않습니다.");
            return;
        }
        ChannelInviteRequest invitation = new ChannelInviteRequest(channel, toUser.getId());
        channelInviteRepository.save(invitation);
    }

    public List<ChannelInviteRequest> getChannelInviteRequest(UUID id){
        List<ChannelInviteRequest> myRequest = channelInviteRepository.findById(id);
        return myRequest;
    }

    public void acceptChannelInviteRequest(User user, ChannelInviteRequest request){
        UUID channelId = request.getChannelId();
        Channel channel = channelService.getChannel(channelId);
        ChannelUser channelUser = new ChannelUser(user);
        channel.getMembers().add(channelUser);
        user.getMyChannels().add(channel);
        channelInviteRepository.remove(user.getId(), request);
    }
}
