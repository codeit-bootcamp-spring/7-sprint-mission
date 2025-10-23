package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.friendrequest.infrastructure.MemoryFriendRequestRepository;
import com.sprint.mission.discodeit.friendship.infrastructure.MemoryFriendShipRepository;
import com.sprint.mission.discodeit.messageroom.infrastructure.MemoryMessageRoomRepository;
import com.sprint.mission.discodeit.friendrequest.application.FriendRequestRepository;
import com.sprint.mission.discodeit.server.application.ServerRepository;
import com.sprint.mission.discodeit.server.presentation.ServerService;
import com.sprint.mission.discodeit.server.application.BasicServerService;
import com.sprint.mission.discodeit.friendship.application.FriendShipRepository;
import com.sprint.mission.discodeit.messageroom.application.BasicMessageRoomService;
import com.sprint.mission.discodeit.messageroom.application.MessageRoomRepository;
import com.sprint.mission.discodeit.messageroom.presentation.MessageRoomService;
import com.sprint.mission.discodeit.server.infrastructure.MemoryServerRepository;
import com.sprint.mission.discodeit.user.application.BasicUserService;
import com.sprint.mission.discodeit.user.application.UserRepository;
import com.sprint.mission.discodeit.user.infrastructure.MemoryUserRepository;
import lombok.Getter;

//의존성 주입
@Getter
public class DiContainer {

//    private final UserRepository userRepository = new MemoryUserRepository();
//    private final MessageRoomRepository messageRoomRepository = new MemoryMessageRoomRepository();
//    private final ServerRepository serverRepository = new MemoryServerRepository();
//    private final FriendRequestRepository friendRequestRepository = new MemoryFriendRequestRepository();
//    private final FriendShipRepository friendShipRepository = new MemoryFriendShipRepository();

//    private final UserRepository userRepository=new FileUserRepository();
//    private final MessageRoomRepository messageRoomRepository=new FileMessageRoomRepository();
//    private final ServerRepository serverRepository = new FileServerRepository();
//    private final FriendRequestRepository friendRequestRepository = new FileFriendRequestRepository();
//    private final FriendShipRepository friendShipRepository =new FileFriendShipRepository();

//    private final com.sprint.mission.discodeit.user.presentation.UserService userService = new BasicUserService(userRepository, friendRequestRepository, friendShipRepository);
//    private final MessageRoomService messageRoomService = new BasicMessageRoomService(messageRoomRepository);
//    private final ServerService serverService = new BasicServerService(serverRepository);
}
