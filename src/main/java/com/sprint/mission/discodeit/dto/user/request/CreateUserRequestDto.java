package com.sprint.mission.discodeit.dto.user.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateUserRequestDto {
    private String userName;
    private String nickName;
    private String email;
    private String phoneNum;
    private String loginId;
    private String password;
}
