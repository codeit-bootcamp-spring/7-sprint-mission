package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.DeletedChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.UUID;

public interface ChannelRepository {

    public ChannelDto getChannelById(UUID channelId);
    public ChannelDto getChannelByName(String channelName);
    public ChannelDto getChannel(ChannelDto channelDto);

    public void saveChannel(ChannelDto channelDto);
    public void deleteChannel(ChannelDto channelDto);
    public <T>void updateChannel(ChannelDto channelDto, Channel.channelElement channelElement, T updatedContent);

    public ChannelDto[] getAllChannel();
    public ChannelDto[] getUpdatedChannel();
    public DeletedChannelDto[] getDeletedChannel();
    public void addUserToChannel(UserDto userDto, ChannelDto channelDto);
    public void deleteUserFromChannel(UserDto userDto, ChannelDto channelDto);

    public void resetChannelRepository();
}
