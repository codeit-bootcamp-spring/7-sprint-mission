package com.sprint.mission.discodeit.dto.user.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UpdatePasswordRequestDto {
    private UUID userId;
    private String newPassword;
}
