package com.sprint.mission.discodeit.dto.user.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateUserRequestDto {
    private String newUserName;
    private String newNickName;
    private String newEmail;
    private String newPassword;
    private String newPhoneNum;
}
