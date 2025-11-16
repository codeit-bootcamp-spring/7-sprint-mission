package com.sprint.mission.discodeit.facade.auth;

import java.time.LocalDateTime;

public record EmailAuth(
    String code,
    LocalDateTime expireAt) {

}
