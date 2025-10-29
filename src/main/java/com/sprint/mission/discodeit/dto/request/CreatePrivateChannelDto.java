package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.enum_.ChannelType;

import java.util.List;
import java.util.UUID;

public record CreatePrivateChannelDto (

    String channelName, //채널 이름
    String description, //채널 설명
    ChannelType channelType,
    List<UUID> members
) {}
