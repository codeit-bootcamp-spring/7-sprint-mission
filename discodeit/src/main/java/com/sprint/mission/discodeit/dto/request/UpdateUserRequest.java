package com.sprint.mission.discodeit.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter @NoArgsConstructor @AllArgsConstructor
public class UpdateUserRequest {
    private UUID userId;
    private String username;
    private String email;
    private String password;
    private CreateBinaryContentRequest profile;
}
