package com.sprint.mission.discodeit.dto.request.channel;

import com.sprint.mission.discodeit.entityElement.ChannelElement;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ChannelUpdateRequestDto<T>{
    private UUID channelId;
    private ChannelElement type;
    private T updatedValue;
}
