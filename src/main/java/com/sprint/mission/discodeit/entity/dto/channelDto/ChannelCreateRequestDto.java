package com.sprint.mission.discodeit.entity.dto.channelDto;

import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

@Getter
@Builder
public class ChannelCreateRequestDto {

    private String channelName;
    @NonNull
    private ChannelType type;    // 채널타입
    @NonNull
    private UUID adminId;

}
