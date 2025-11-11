package com.sprint.mission.discodeit.dto.channel.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class CreatePrivateChannelRequestDto {
    private final List<UUID> participantIds;
}
