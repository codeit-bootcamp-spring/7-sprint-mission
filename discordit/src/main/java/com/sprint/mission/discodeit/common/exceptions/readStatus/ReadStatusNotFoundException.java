package com.sprint.mission.discodeit.common.exceptions.readStatus;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public class ReadStatusNotFoundException extends RuntimeException{
    public ReadStatusNotFoundException(User user, Channel channel) {
        super(user.getUserId() + ", " +  channel.getChannelName() + "는 존재하지 않습니다.");
    }

    public ReadStatusNotFoundException(ReadStatus readStatus) {
        super(readStatus.getUser().getUserId() + ", " +
                readStatus.getChannel().getChannelName() + "는 존재하지 않습니다.");
    }

    public ReadStatusNotFoundException(UUID uuid) {
        super("존재하지 않는 아이디입니다 : " + uuid);
    }


}
