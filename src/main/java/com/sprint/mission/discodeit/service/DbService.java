package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

public interface DbService {
    public void deleteUser(User user);
    public void deleteMessage(Message message);
    public void deleteChannel(Channel channel);

    public void createUser(User user);
    public void createMessage(Message message);
    public void createChannel(Channel channel);


}
