package com.sprint.mission.discodeit.dto.fileIo;


import com.sprint.mission.discodeit.enums.ReceiverType;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * 파일 저장용 메시지 DTO
 * User와 Receivable 객체 대신 UUID만 저장하여 직렬화 문제를 해결합니다.
 */
@Getter
public class MessageIoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // Getters
    private UUID uuid;
    private Instant createdAt;
    private Instant updatedAt;
    private UUID senderUuid;
    private UUID receiverUuid;
    private ReceiverType receiverType; // "USER" 또는 "CHANNEL"
    private String message;
    private List<UUID> attachmentIds;

    public MessageIoDTO(UUID uuid, Instant createdAt, Instant updatedAt,
                        UUID senderUuid,
                        UUID receiverUuid, ReceiverType receiverType,
                        String message, List<UUID> attachments) {
        this.uuid = uuid;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.senderUuid = senderUuid;
        this.receiverUuid = receiverUuid;
        this.receiverType = receiverType;
        this.message = message;
        this.attachmentIds = attachments;
    }

}
