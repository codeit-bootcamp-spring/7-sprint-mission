package com.sprint.mission.discodeit.dto.user.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateUserRequestDto {
    private final String userName;
    private final String nickName;
    private final String email;
    private final String phoneNum;
    private final String loginId;
    private final String password;
}
