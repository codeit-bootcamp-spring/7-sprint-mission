package com.sprint.mission.discodeit.dto.update;

import com.sprint.mission.discodeit.enum_.ChannelType;

public record UpdateChannelDto (
        String channelName,
        String description, //채널 설명
        ChannelType channelType //채널 타입
) {}
