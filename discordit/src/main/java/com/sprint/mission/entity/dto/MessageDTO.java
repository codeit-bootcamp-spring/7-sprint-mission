package com.sprint.mission.entity.dto;

import java.io.Serializable;
import java.util.UUID;

/**
 * 파일 저장용 메시지 DTO
 * User와 Receivable 객체 대신 UUID만 저장하여 직렬화 문제를 해결합니다.
 */
public class MessageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID uuid;
    private Long createdAt;
    private Long updatedAt;
    private UUID senderUuid;
    private String senderUserId; // User는 userId로 찾기 위함
    private UUID receiverUuid;
    private String receiverType; // "USER" 또는 "CHANNEL"
    private String message;

    public MessageDTO(UUID uuid, Long createdAt, Long updatedAt,
                      UUID senderUuid, String senderUserId,
                      UUID receiverUuid, String receiverType, String message) {
        this.uuid = uuid;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.senderUuid = senderUuid;
        this.senderUserId = senderUserId;
        this.receiverUuid = receiverUuid;
        this.receiverType = receiverType;
        this.message = message;
    }

    // Getters
    public UUID getUuid() {
        return uuid;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public UUID getSenderUuid() {
        return senderUuid;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public UUID getReceiverUuid() {
        return receiverUuid;
    }

    public String getReceiverType() {
        return receiverType;
    }

    public String getMessage() {
        return message;
    }
}

