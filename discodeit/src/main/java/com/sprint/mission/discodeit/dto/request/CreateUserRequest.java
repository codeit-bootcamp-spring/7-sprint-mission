package com.sprint.mission.discodeit.dto.request;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequest {
    private String username;
    private String password;
    private String email;
    private CreateBinaryContentRequest profile;
}
