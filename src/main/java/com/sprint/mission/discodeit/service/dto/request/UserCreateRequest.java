package com.sprint.mission.discodeit.service.dto.request;

public record UserCreateRequest(
        String email,
        String password,
        String username

)
 {}