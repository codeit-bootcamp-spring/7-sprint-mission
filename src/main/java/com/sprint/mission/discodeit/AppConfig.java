package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.application.ConsoleApplication;
import com.sprint.mission.discodeit.repository.jcf.*;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.service.jcf.*;
import com.sprint.mission.discodeit.service.repository.*;
import lombok.Getter;

//의존성 주입
@Getter
public class AppConfig {

//    private final UserRepository userRepository=new MemoryUserRepository();
//    private final MessageRoomRepository messageRoomRepository=new MemoryMessageRoomRepository();
//    private final ChannelRepository channelRepository = new MemoryChannelRepository();
//    private final FriendRequestRepository friendRequestRepository = new MemoryFriendRequestRepository();
//    private final FriendShipRepository friendShipRepository =new MemoryFriendShipRepository();

    private final UserRepository userRepository=new JCFUserRepository();
    private final MessageRoomRepository messageRoomRepository=new JCFMessageRoomRepository();
    private final ChannelRepository channelRepository = new JCFChannelRepository();
    private final FriendRequestRepository friendRequestRepository = new JCFFriendRequestRepository();
    private final FriendShipRepository friendShipRepository =new JCFriendShipRepository();

    private final UserService userService = new JCFUserService(userRepository);
    private final MessageRoomService messageRoomService= new JCFMessageRoomService(messageRoomRepository);
    private final ChannelService channelService = new JCFChannelService(channelRepository);
    private final FriendRequestService friendRequestService = new JCFFriendRequestService(friendRequestRepository);
    private final FriendShipService friendShipService = new JCFFriendShipService(friendShipRepository);

    private final ConsoleApplication consoleApplication =
    new ConsoleApplication(channelService, userService, messageRoomService, friendRequestService, friendShipService);



}
