package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;


public class JCFServiceFactory {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    private final UserService userService;
    private final ChannelService channelService;
    private final MessageService<User> messageService;
    private final MessageService<Channel> channelMessageService;


    private JCFServiceFactory() {
        messageRepository = new JCFMessageRepository();
        userRepository = new JCFUserRepository();

        userService = new JCFUserService(userRepository, messageRepository);
        channelService = new JCFChannelService(userService);
        messageService = new JCFMessageService<>(messageRepository);
        channelMessageService = new JCFMessageService<>(messageRepository);
    }

    // 팩토리 클래스는 한 번만 부르면 되므로 싱글톤 패턴으로 구현
    static JCFServiceFactory s = new JCFServiceFactory();

    public static JCFServiceFactory getInstance() {
        return s;
    }

    public UserService getUserService() {
        return userService;
    }

    public ChannelService getChannelService() {
        return channelService;
    }

    public MessageService<User> getMessageService() {
        return messageService;
    }

    public MessageService<Channel> getChannelMessageService() {
        return channelMessageService;
    }

}
