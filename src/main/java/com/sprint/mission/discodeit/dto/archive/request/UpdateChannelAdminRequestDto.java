package com.sprint.mission.discodeit.dto.archive.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UpdateChannelAdminRequestDto {
    UUID adminId;
    UUID newAdminId;
}
