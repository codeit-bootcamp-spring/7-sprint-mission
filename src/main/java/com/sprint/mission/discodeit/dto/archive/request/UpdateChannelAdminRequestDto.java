package com.sprint.mission.discodeit.dto.archive.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UpdateChannelAdminRequestDto {
    private final UUID adminId;
    private final UUID newAdminId;
}
