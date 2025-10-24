package com.sprint.mission.discodeit.binarycontent.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;
@Getter
@RequiredArgsConstructor
public class BinaryContent {

    private final UUID id;
    private final Instant createdAt;

    private String fileName;  // UUID.toString()
    private String filePath;  // uploads/user123/profile.png
    private String fileType;  // image/png
    private long fileSize;    // 1024

    public BinaryContent(String fileName, String fileType, String filePath, long fileSize) {
        this.id=UUID.randomUUID();
        this.createdAt=Instant.now();
        this.fileName = fileName;
        this.fileType = fileType;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }

}
