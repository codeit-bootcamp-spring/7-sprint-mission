package com.sprint.mission.discodeit.exceptions;

import jakarta.validation.constraints.NotNull;

public class ReceiverNotFoundException extends RuntimeException {
    public ReceiverNotFoundException(String uuid) {
        super("수신자가 조회되지 않습니다. 다시 한번 확인해주세요.");
    }
}
