package com.sprint.mission.discodeit.user.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 현재 프로젝트 Api스펙에 맞춰 재 성성된 UpdateRequest입니다
 */
public record UserUpdateRequest(
    String newUsername,
    String newEmail,
    String newPassword

) {

}
