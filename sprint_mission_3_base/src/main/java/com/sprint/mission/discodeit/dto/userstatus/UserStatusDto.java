package com.sprint.mission.discodeit.dto.userstatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusDto(
        UUID id,
        UUID userId,
        Instant lastSeenAt,
        boolean online // 5분 이내 접속 여부 계산값
) {}
