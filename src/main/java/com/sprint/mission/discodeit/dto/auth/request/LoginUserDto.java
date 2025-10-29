package com.sprint.mission.discodeit.dto.auth.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginUserDto {
    private String userId;
    private String password;
}
