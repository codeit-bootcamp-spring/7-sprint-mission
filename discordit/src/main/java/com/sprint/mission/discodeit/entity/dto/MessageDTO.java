package com.sprint.mission.discodeit.entity.dto;


import com.sprint.mission.discodeit.entity.base.ReceiverType;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * 파일 저장용 메시지 DTO
 * User와 Receivable 객체 대신 UUID만 저장하여 직렬화 문제를 해결합니다.
 */
@Getter
public class MessageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // Getters
    private UUID uuid;
    private Instant createdAt;
    private Instant updatedAt;
    private UUID senderUuid;
    private String senderUserId; // User는 userId로 찾기 위함
    private UUID receiverUuid;
    private ReceiverType receiverType; // "USER" 또는 "CHANNEL"
    private String message;

    public MessageDTO(UUID uuid, Instant createdAt, Instant updatedAt,
                      UUID senderUuid, String senderUserId,
                      UUID receiverUuid, ReceiverType receiverType, String message) {
        this.uuid = uuid;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.senderUuid = senderUuid;
        this.senderUserId = senderUserId;
        this.receiverUuid = receiverUuid;
        this.receiverType = receiverType;
        this.message = message;
    }

}

