package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.jcf.*;

//의존성 주입
public class AppConfig {

    private final MemoryUserRepository userRepository=new MemoryUserRepository();
    private final MemoryMessageRoomRepository messageRoomRepository=new MemoryMessageRoomRepository();
    private final MemoryChannelRepository channelRepository = new MemoryChannelRepository();
    private final MemoryFriendRequestRepository friendRequestRepository = new MemoryFriendRequestRepository();
    private final MemoryChannelInviteRepository channelInviteRepository = new MemoryChannelInviteRepository();

    private final JCFUserService userService = new JCFUserService(userRepository);
    private final JCFMessageRoomService messageRoomService= new JCFMessageRoomService(messageRoomRepository);
    private final JCFChannelService channelService = new JCFChannelService(channelRepository);
    private final ChannelInviteRequestService channelInviteRequestService = new ChannelInviteRequestService(channelInviteRepository,channelService);
    private final JCFFriendRequestService friendRequestService =new JCFFriendRequestService(friendRequestRepository,userService);

    //getter
    public JCFUserService userService(){
        return userService;
    }
    public JCFMessageRoomService messageRoomService(){
        return messageRoomService;
    }

    public JCFChannelService channelService(){
        return channelService;
    }

    public JCFFriendRequestService getFriendRequestService() {
        return friendRequestService;
    }

    public ChannelInviteRequestService getChannelInviteRequestService() {
        return channelInviteRequestService;
    }
}
