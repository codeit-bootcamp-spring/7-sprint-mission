package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.repository.file.*;
import com.sprint.mission.discodeit.application.*;
import com.sprint.mission.discodeit.application.jcf.*;
import com.sprint.mission.discodeit.application.repository.*;
import lombok.Getter;

//의존성 주입
@Getter
public class AppConfig {

//    private final UserRepository userRepository=new MemoryUserRepository();
//    private final MessageRoomRepository messageRoomRepository=new MemoryMessageRoomRepository();
//    private final ChannelRepository channelRepository = new MemoryChannelRepository();
//    private final FriendRequestRepository friendRequestRepository = new MemoryFriendRequestRepository();
//    private final FriendShipRepository friendShipRepository =new MemoryFriendShipRepository();

    private final UserRepository userRepository=new FileUserRepository();
    private final MessageRoomRepository messageRoomRepository=new FileMessageRoomRepository();
    private final ChannelRepository channelRepository = new FileChannelRepository();
    private final FriendRequestRepository friendRequestRepository = new FileFriendRequestRepository();
    private final FriendShipRepository friendShipRepository =new FileFriendShipRepository();

    private final UserService userService = new JCFUserService(userRepository);
    private final MessageRoomService messageRoomService= new JCFMessageRoomService(messageRoomRepository);
    private final ChannelService channelService = new JCFChannelService(channelRepository);
}
