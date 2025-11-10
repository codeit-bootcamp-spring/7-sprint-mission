package com.sprint.mission.discodeit.dto.channel.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UpdateChannelNameRequestDto {
    private final UUID adminId;
    private final String name;
}
