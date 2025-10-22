package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.friendrequest.infrastructure.MemoryFriendRequestRepository;
import com.sprint.mission.discodeit.friendship.infrastructure.MemoryFriendShipRepository;
import com.sprint.mission.discodeit.messageroom.infrastructure.MemoryMessageRoomRepository;
import com.sprint.mission.discodeit.friendrequest.infrastructure.FriendRequestRepository;
import com.sprint.mission.discodeit.server.application.ServerRepository;
import com.sprint.mission.discodeit.server.application.ServerService;
import com.sprint.mission.discodeit.server.application.FileServerService;
import com.sprint.mission.discodeit.friendship.infrastructure.FriendShipRepository;
import com.sprint.mission.discodeit.messageroom.application.FileMessageRoomService;
import com.sprint.mission.discodeit.messageroom.application.MessageRoomRepository;
import com.sprint.mission.discodeit.messageroom.application.MessageRoomService;
import com.sprint.mission.discodeit.server.infrastructure.MemoryServerRepository;
import com.sprint.mission.discodeit.user.application.FileUserService;
import com.sprint.mission.discodeit.user.application.UserRepository;
import com.sprint.mission.discodeit.user.application.UserService;
import com.sprint.mission.discodeit.user.infrastructure.MemoryUserRepository;
import lombok.Getter;

//의존성 주입
@Getter
public class WebConfig {

    private final UserRepository userRepository = new MemoryUserRepository();
    private final MessageRoomRepository messageRoomRepository = new MemoryMessageRoomRepository();
    private final ServerRepository serverRepository = new MemoryServerRepository();
    private final FriendRequestRepository friendRequestRepository = new MemoryFriendRequestRepository();
    private final FriendShipRepository friendShipRepository = new MemoryFriendShipRepository();

//    private final UserRepository userRepository=new FileUserRepository();
//    private final MessageRoomRepository messageRoomRepository=new FileMessageRoomRepository();
//    private final ServerRepository serverRepository = new FileServerRepository();
//    private final FriendRequestRepository friendRequestRepository = new FileFriendRequestRepository();
//    private final FriendShipRepository friendShipRepository =new FileFriendShipRepository();

    private final UserService userService = new FileUserService(userRepository, friendRequestRepository, friendShipRepository);
    private final MessageRoomService messageRoomService = new FileMessageRoomService(messageRoomRepository);
    private final ServerService serverService = new FileServerService(serverRepository);
}
