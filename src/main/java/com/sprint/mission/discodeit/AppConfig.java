package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRoomRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.*;
import com.sprint.mission.discodeit.service.jcf.*;
import lombok.Getter;

//의존성 주입
@Getter
public class AppConfig {

    private final UserRepository userRepository=new MemoryUserRepository();
    private final MessageRoomRepository messageRoomRepository=new MemoryMessageRoomRepository();
    private final ChannelRepository channelRepository = new MemoryChannelRepository();

    private final JCFUserService userService = new JCFUserService(userRepository);
    private final JCFMessageRoomService messageRoomService= new JCFMessageRoomService(messageRoomRepository);
    private final JCFChannelService channelService = new JCFChannelService(channelRepository);


}
