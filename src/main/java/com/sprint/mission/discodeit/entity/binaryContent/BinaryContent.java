package com.sprint.mission.discodeit.entity.binaryContent;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent {

    private final UUID id;
    private final Instant createAt;

    private final byte[] binaryData;
    private final String dataName;
    private final String dataType;    // txt, zip, jpg등

    private final UUID userId;  // 프로필
    private final UUID messageId;   // 메시지첨부


    public BinaryContent(UUID userId, UUID messageId, byte[] binaryData, String dataName, String dataType) {
        this.id = UUID.randomUUID();
        this.createAt = Instant.now();
        this.userId = userId;
        this.messageId = messageId;
        this.binaryData = binaryData;
        this.dataName = dataName;
        this.dataType = dataType;
    }

    public BinaryContent(UUID userId, byte[] binaryData, String dataName, String dataType) {
        this(userId, null, binaryData, dataName, dataType);
    }


    // 메시지가 채널과 개인 2개라 일단 프로필사진과 채널에 대해서 구현
}
