package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.request.channel.ChannelPatchRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.ChannelPrivateCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.ChannelPublicCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.ChannelReadResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService {


    void createChannel(Channel channel);
    ChannelReadResponseDto createPrivateChannel(ChannelPrivateCreateRequestDto channelPrivateCreateRequestDto);
    ChannelReadResponseDto createPublicChannel(ChannelPublicCreateRequestDto channelPublicCreateRequestDto);
    ChannelReadResponseDto patchChannel(ChannelPatchRequestDto dto, UUID channelId);
    ChannelReadResponseDto readChannel(UUID channelId);

    List<ChannelReadResponseDto> findAllByUserId(UUID userId);
    void deleteChannel(UUID channelId);
    <T> void updateChannel(ChannelUpdateRequestDto<T> channelUpdateRequestDto);
    List<Channel> readUpdatedChannel();
    //void readDeletedChannel();
    void inviteUserToChannel(UUID userId, UUID channelId);

    void deleteUserFromChannel(UUID userId, UUID channelId);
    void resetChannelRepository();
    List<ChannelReadResponseDto> readAllChannel();
}
