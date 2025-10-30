package com.sprint.mission.discodeit.dto.request;

import java.util.UUID;

public record UserUpdateReq (
        String nickname,
        String password,
        UUID profileId
){
}
