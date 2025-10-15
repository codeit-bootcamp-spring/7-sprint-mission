package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

public interface ChannelService {


    void createChannel(ChannelDto channelDto);
    void readChannel(ChannelDto channelDto);
    void readAllChannel();
    void deleteChannel(ChannelDto channelDto);
    <T> void updateChannel(ChannelDto channel, Channel.channelElement channelElement, T updatedContent);
    void readUpdatedChannel();
    void readDeletedChannel();
    void inviteUserToChannel(UserDto user, ChannelDto channel);
    void deleteUserFromChannel(UserDto user, ChannelDto channel);
}
