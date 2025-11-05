package com.sprint.mission.discodeit.dto.request.authService;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRequestDto {
    String userName;
    String password;
}
