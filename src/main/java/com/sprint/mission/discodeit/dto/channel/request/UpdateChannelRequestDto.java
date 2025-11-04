package com.sprint.mission.discodeit.dto.channel.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UpdateChannelRequestDto {
    UUID userId;
}
