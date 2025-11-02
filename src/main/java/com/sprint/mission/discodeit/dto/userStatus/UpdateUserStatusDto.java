package com.sprint.mission.discodeit.dto.userStatus;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class UpdateUserStatusDto {
    UUID userStatusId;
    Instant loginAt;
}
