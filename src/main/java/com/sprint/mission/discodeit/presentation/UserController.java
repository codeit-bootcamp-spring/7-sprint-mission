package com.sprint.mission.discodeit.presentation;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public User createUserController(UserDto userDto){
        User user = userInverter(userDto);
        userService.createUser(user);
        return user;
    }
    public void deleteUserController(UserDto userDto){
        User user = userInverter(userDto);
        userService.deleteUser(user);
    }
    public void readUserController(UserDto userDto){
        User user = userInverter(userDto);
        userService.readUser(user);
    }
    public void readUpdatedUserController(){
        userService.readUpdatedUser();
    }
    public void readDeletedUserController(){
        userService.readDeletedUser();
    }
    public void readAllUserController(){
        userService.readAllUser();
    }
    public void updateUserController(UserDto userDto, User.userElement userElement, Object updatedContent){
        User user = userInverter(userDto);
        userService.updateUser(user, userElement, updatedContent);
    }
    public void enterChannelController(UserDto userDto, ChannelDto channelDto){
        User user = userInverter(userDto);
        Channel channel = channelInverter(channelDto);
        userService.enterChannel(user, channel);
    }
    public void exitChannelController(UserDto userDto, ChannelDto channelDto){
        User user = userInverter(userDto);
        Channel channel = channelInverter(channelDto);
        userService.exitChannel(user, channel);
    }



    private User userInverter(UserDto userDto){
        return new User(userDto.getId(),
                userDto.getName(),
                userDto.getNickname(),
                userDto.getEmail(),
                userDto.isOnline());
    }
    private Channel channelInverter(ChannelDto channelDto){
        return new Channel(channelDto.getId(),
                channelDto.getName(),
                channelDto.getDescription(),
                channelDto.isPublic(),
                channelDto.isTextChannel());
    }
}
