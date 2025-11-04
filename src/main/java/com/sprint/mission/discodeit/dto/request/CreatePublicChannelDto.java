package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.enum_.ChannelType;


public record CreatePublicChannelDto (

    String channelName, //채널 이름
    String description, //채널 설명
    ChannelType channelType //채널 타입
) {}
