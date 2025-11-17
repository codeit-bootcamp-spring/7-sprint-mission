package com.sprint.mission.discodeit.dto.update;

import java.util.UUID;

public record UpdateUserDto(
    String newUsername, // 유저 이름
    String newEmail, // 유저 닉네임
    String newPassword // 비밀번호
) {

}
