package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

// 만들어야됨
public interface ServiceFactory {
    public UserService getUserService();
    public ChannelService getChannelService();
    public MessageService getMessageService();
}
