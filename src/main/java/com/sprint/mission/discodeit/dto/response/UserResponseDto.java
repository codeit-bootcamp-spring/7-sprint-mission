package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record UserResponseDto(

    UUID id,
    String username, // 유저 이름
    String email, // 이메일
    BinaryContentResponseDto profileId, // 프로필 ID
    Boolean online //온라인 상태
) {

}
