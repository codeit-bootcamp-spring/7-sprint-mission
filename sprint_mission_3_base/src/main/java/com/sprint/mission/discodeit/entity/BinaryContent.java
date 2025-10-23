package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private Instant createdAt;   // 수정 불가 모델 → updatedAt 없음

    private BinaryOwnerType ownerType; // USER_PROFILE or MESSAGE_ATTACHMENT
    private UUID ownerId;              // User.id 또는 Message.id

    private String filename;
    private String contentType;
    private byte[] data;

    public BinaryContent(BinaryOwnerType ownerType, UUID ownerId,
                         String filename, String contentType, byte[] data) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.ownerType = ownerType;
        this.ownerId = ownerId;
        this.filename = filename;
        this.contentType = contentType;
        this.data = data;
    }
}
