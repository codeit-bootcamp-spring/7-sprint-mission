package com.sprint.mission.discodeit.exceptions;

import com.sprint.mission.discodeit.entity.base.Channel;
import com.sprint.mission.discodeit.entity.base.User;

public class ReadStatusNotFoundException extends RuntimeException{
    public ReadStatusNotFoundException(User user, Channel channel) {
        super(user.getUserId() + ", " +  channel.getChannelName() + "는 존재하지 않습니다.");
    }
}
