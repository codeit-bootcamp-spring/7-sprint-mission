package com.sprint.mission.discodeit.dto.fileIo;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * 파일 IO용 BinaryContent DTO
 */
@Getter
public class BinaryContentIoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID uuid;
    private Instant uploadedAt;
    private String fileUrl;

    public BinaryContentIoDTO(UUID uuid, Instant uploadedAt, String fileUrl) {
        this.uuid = uuid;
        this.uploadedAt = uploadedAt;
        this.fileUrl = fileUrl;
    }
}
