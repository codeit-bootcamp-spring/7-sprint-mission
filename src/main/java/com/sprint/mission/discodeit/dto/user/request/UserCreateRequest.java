package com.sprint.mission.discodeit.dto.user.request;

import java.util.UUID;

public record UserCreateRequest(
        String username,
        String email,
        String rawPassword,
        String userNickname
        //예상하고 만들어둔거다  이해가 안간다
       ,byte[] profileImage //

) {
}

