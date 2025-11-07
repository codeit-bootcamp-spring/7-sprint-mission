package com.sprint.mission.discodeit.common.exceptions;

import java.util.UUID;

public class BinaryContentNotFoundException extends RuntimeException {
    public BinaryContentNotFoundException(UUID uuid) {
        super(uuid + "는 존재하지 않는 파일입니다.");
    }
}
