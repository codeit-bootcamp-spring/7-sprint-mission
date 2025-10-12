package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

public class JCFServiceFactory {
    private final UserService userService;
    private final ChannelService channelService;
    private final MessageService<User> messageService;
    private final MessageService<Channel> channelMessageService;

    private JCFServiceFactory() {
        messageService = new JCFMessageService<>();
        userService = new JCFUserService(messageService);
        channelService = new JCFChannelService(userService);
        channelMessageService = new JCFMessageService<>();
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
