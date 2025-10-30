package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.entity.Channel;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record ChannelCreateSecReq(
        UUID managerId,
        List<UUID> users
) {
    public Channel to(){
        return  Channel.builder()
                .managerId(managerId)
                .users(users)
                .build();
    }
}
