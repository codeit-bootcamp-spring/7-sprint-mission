package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

public interface ValidateService {
    public boolean isValidateChannel(Channel channel);
    public boolean isValidateUser(User user);
    public boolean isValidateMessage(Message message);
}
