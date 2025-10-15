package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

public interface ValidateService {
    public boolean isValidateChannel(ChannelDto channel);
    public boolean isValidateUser(UserDto user);
    public boolean isValidateMessage(MessageDto message);
}
