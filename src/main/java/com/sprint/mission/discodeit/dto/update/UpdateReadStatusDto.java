package com.sprint.mission.discodeit.dto.update;

import java.time.Instant;

public record UpdateReadStatusDto (
    Instant newReadAt // 읽은 시간
) {}
