package com.sprint.mission.discodeit.application.dto;

import com.sprint.mission.discodeit.content.binary.dto.BinaryContentResponse;
import com.sprint.mission.discodeit.user.dto.UserResponseDTO;
import java.time.Instant;
import java.util.UUID;

public record ProfileUpdateResponse(
    UUID id,
    Instant createDate,
    Instant updateDate,
    String username,
    String password,
    String profileImgPath
) {

  public static ProfileUpdateResponse from(UserResponseDTO user, BinaryContentResponse profileImg) {
    return new ProfileUpdateResponse(
        user.userId(),
        user.createdAt(),
        user.updatedAt(),
        user.username(),
        user.password(),
        profileImg.filePath()
    );
  }
}
