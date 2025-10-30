package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.entity.Channel;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record ChannelCreateReq(
        UUID managerId,
        String name,
        String description
) {
    public Channel to(){
        return  Channel.builder()
                .managerId(managerId)
                .name(name)
                .description(description)
                .build();
    }
}
