package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

public interface UserService {

    void createUser(UserDto userDto);
    void readUser(UserDto userDto);
    void readAllUser();
    void deleteUser(UserDto userDto);
    <T>void updateUser(UserDto userDto,User.userElement userElement, T updatedContent);
    void readUpdatedUser();
    void readDeletedUser();
    void enterChannel(UserDto userDto, ChannelDto channelDto);
    void exitChannel(UserDto userDto,ChannelDto channelDto);

}
