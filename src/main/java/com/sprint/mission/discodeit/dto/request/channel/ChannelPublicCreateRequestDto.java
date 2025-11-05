package com.sprint.mission.discodeit.dto.request.channel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ChannelPublicCreateRequestDto {
    public HashSet<UUID> userIdList;
    public String name ;
    public String description;
    public boolean isTextChannel;
}
