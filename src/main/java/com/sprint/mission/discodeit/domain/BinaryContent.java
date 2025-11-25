package com.sprint.mission.discodeit.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
public class BinaryContent{

    private UUID id;
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
