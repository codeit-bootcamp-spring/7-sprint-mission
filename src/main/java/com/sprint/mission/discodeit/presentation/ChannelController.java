package com.sprint.mission.discodeit.presentation;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

public class ChannelController {

    private final ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    public Channel createChannelController(ChannelDto channelDto){
        Channel channel = channelInverter(channelDto);
        channelService.createChannel(channel);
        return channel;
    }
    public void deleteChannelController(ChannelDto channelDto){
        Channel channel = channelInverter(channelDto);
        channelService.deleteChannel(channel);
    }
    public void updateChannelController(ChannelDto channelDto, Channel.channelElement channelElement, int index){
        Channel channel = channelInverter(channelDto);
        channelService.updateChannel(channel, channelElement, index);
    }
    public void readChannelController(ChannelDto channelDto){
        Channel channel = channelInverter(channelDto);
        channelService.readChannel(channel);

    }
    public void readUpdatedChannelController(){
        channelService.readUpdatedChannel();
    }
   public void readDeletedChannelController(){
        channelService.readDeletedChannel();
   }
    public void inviteUserToChannelController(UserDto userDto, ChannelDto channelDto){
        User user = userInverter(userDto);
        Channel channel = channelInverter(channelDto);
        channelService.inviteUserToChannel(user, channel);
    }
    public void deleteUserFromChannelController(UserDto userDto, ChannelDto channelDto){
        User user = userInverter(userDto);
        Channel channel = channelInverter(channelDto);
        channelService.deleteUserFromChannel(user, channel);
    }

    private Channel channelInverter(ChannelDto channelDto){
        return new Channel(channelDto.getId(),
                channelDto.getName(),
                channelDto.getDescription(),
                channelDto.isPublic(),
                channelDto.isTextChannel()
        );
    }

    private User userInverter(UserDto userDto){

        return new User(userDto.getId(),
                userDto.getName(),
                userDto.getNickname(),
                userDto.getEmail(),
                userDto.isOnline()

        );
    }
}
