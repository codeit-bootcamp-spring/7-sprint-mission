package com.sprint.mission.discodeit.entity;

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

    // 요구사항에 맞춰 삭제
//    private final UUID userId;  // 프로필
//    private final UUID messageId;   // 메시지첨부


    public BinaryContent(byte[] binaryData, String dataName, String dataType) {
        this.id = UUID.randomUUID();
        this.createAt = Instant.now();
        this.binaryData = binaryData;
        this.dataName = dataName;
        this.dataType = dataType;
    }

}
