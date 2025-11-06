package com.sprint.mission.discodeit.dto.user.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateUserRequestDto {
    private final String newUserName;
    private final String newNickName;
    private final String newEmail;
    private final String newPassword;
    private final String newPhoneNum;
}
