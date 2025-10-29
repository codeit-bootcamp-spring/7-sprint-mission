package com.sprint.mission.discodeit.dto.channel.request;

import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CreateChannelRequestDto {
    private ChannelType channelType;
    private String channelName;
    private UUID adminId;
}
