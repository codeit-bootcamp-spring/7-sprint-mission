package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.DeletedUserDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

public interface UserRepository {

    public UserDto getUserById(UUID userId) throws IOException, ClassNotFoundException;



    public UserDto getUserByName(String userName);
    public UserDto getUser(UserDto userDto);
    public UserDto[] getAllUser();
    public void saveUser(UserDto userDto);
    public void deleteUser(UserDto userDto);
    public <T> void updateUser(UserDto userDto, User.userElement userElement, T updatedContent);
    public UserDto[] getUpdatedUser();
    public DeletedUserDto[] getDeletedUser();
    public void addChannelToUser(UserDto userDto, ChannelDto channelDto);
    public void deleteChannelFromUser(UserDto userDto, ChannelDto channelDto);
    public void resetUserRepository();


}
