package com.sprint.mission.discodeit.dto.request.channel;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashSet;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ChannelPublicCreateRequestDto {
    public HashSet<UUID> participantIds;
    public String name ;
    public String description;
    public boolean isTextChannel;
}
