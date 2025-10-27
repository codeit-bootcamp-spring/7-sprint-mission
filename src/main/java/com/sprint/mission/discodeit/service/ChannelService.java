package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.CreateChannelDto;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createChannel(CreateChannelDto createChannelDto);
    Channel getChannel(UUID channelId);
    List<Channel> getAllChannels();
    void updateChannel(UpdateChannelDto updateChannelDto);
    void deleteChannel(UUID channelId);
    boolean isExistsChannel(UUID channelId);

    // 채널에 유저 등록
    void addUserToChannel(Channel channel, User user);
    // 참여 중인 유저 삭제
    void removeUserFromChannel(Channel channel, User user);
}
