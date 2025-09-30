package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.repository.MemoryChannelRepository;
import com.sprint.mission.discodeit.repository.MemoryMessageRoomRepository;
import com.sprint.mission.discodeit.repository.MemoryUserRepository;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageRoomService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

//의존성 주입
public class AppConfig {

    private final MemoryUserRepository userRepository=new MemoryUserRepository();
    private final MemoryMessageRoomRepository messageRoomRepository=new MemoryMessageRoomRepository();
    private final MemoryChannelRepository channelRepository = new MemoryChannelRepository();

    private final JCFUserService userService = new JCFUserService(userRepository);
    private final JCFMessageRoomService messageRoomService= new JCFMessageRoomService(messageRoomRepository);
    private final JCFChannelService channelService = new JCFChannelService(channelRepository);


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

}
