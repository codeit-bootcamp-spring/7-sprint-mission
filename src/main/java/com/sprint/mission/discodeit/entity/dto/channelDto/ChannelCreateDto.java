package com.sprint.mission.discodeit.entity.dto.channelDto;

import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ChannelCreateDto {

    private String channelName;
    private ChannelType type;    // 채널타입
    private UUID adminId;

}
