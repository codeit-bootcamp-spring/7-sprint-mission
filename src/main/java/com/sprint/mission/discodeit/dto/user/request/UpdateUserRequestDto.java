package com.sprint.mission.discodeit.dto.user.request;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Getter
public class UpdateUserRequestDto {
    private UUID userId;
    private String updateParam;
    private MultipartFile profileImage;
    private UpdateType type;

    private UpdateUserRequestDto(UUID userId, UpdateType type) {
        this.userId = userId;
        this.type = type;
    }

    public UpdateUserRequestDto(UUID userId, String updateParam, UpdateType type) {
        this(userId, type);
        this.updateParam = updateParam;
        this.profileImage = null;
    }

    public UpdateUserRequestDto(UUID userId, MultipartFile profileImage, UpdateType type) {
        this(userId, type);
        this.updateParam = null;
        this.profileImage = profileImage;
    }
}
