package com.sprint.mission.discodeit.dto.user.request;

import java.util.UUID;

public record UserUpdateReq (
        String email,
        String nickname,
        String password,
        UUID profileId
){
}
