package com.sprint.mission.discodeit.dto.req;

public record UserCreatedReq(
        String email,
        String nickname,
        String password
){}
