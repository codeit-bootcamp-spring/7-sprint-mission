package com.sprint.mission.discodeit.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;
@Getter
public class BinaryContent{

    private String id;
    private Instant createdAt;

    private String fileName;  // UUID.toString()
    private String filePath;  // uploads/user123/profile.png
    private String fileType;  // image/png
    private long fileSize;    // 1024

    public BinaryContent(String fileName, String fileType, String filePath, long fileSize) {

        this.fileName = fileName;
        this.fileType = fileType;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }
}
