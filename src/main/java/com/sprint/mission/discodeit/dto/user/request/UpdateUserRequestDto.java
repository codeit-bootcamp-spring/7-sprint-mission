package com.sprint.mission.discodeit.dto.user.request;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UpdateUserRequestDto {
    private UUID userId;
    private String updateParam;
    private byte[] updateProfile;
    private UpdateType type;

    private UpdateUserRequestDto(UUID userId, UpdateType type) {
        this.userId = userId;
        this.type = type;
    }

    public UpdateUserRequestDto(UUID userId, String updateParam, UpdateType type) {
        this(userId, type);
        this.updateParam = updateParam;
        this.updateProfile = null;
    }

    public UpdateUserRequestDto(UUID userId, byte[] updateProfile, UpdateType type) {
        this(userId, type);
        this.updateParam = "";
        this.updateProfile = updateProfile;
    }
}
