package com.sprint.mission.discodeit.dto.archive.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class DeleteChannelRequestDto {
    private final UUID userId;
}
