package com.sprint.mission.discodeit.entity.binaryContent;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent {

    private final UUID id;
    private final Instant createAt;

    private final byte[] binaryData;
    private final String fileName;
    private final String fileType;    // txt, zip, jpg등

    private final UUID userId;  // 프로필
    private final UUID messageId;   // 메시지첨부


    public BinaryContent(UUID userId, UUID messageId, byte[] binaryData, String fileName, String fileType) {
        this.id = UUID.randomUUID();
        this.createAt = Instant.now();
        this.userId = userId;
        this.messageId = messageId;
        this.binaryData = binaryData;
        this.fileName = fileName;
        this.fileType = fileType;
    }

    public BinaryContent(UUID userId, byte[] binaryData, String fileName, String fileType) {
        this(userId, null, binaryData, fileName, fileType);
    }


    // 메시지가 채널과 개인 2개라 일단 프로필사진과 채널에 대해서 구현
}
