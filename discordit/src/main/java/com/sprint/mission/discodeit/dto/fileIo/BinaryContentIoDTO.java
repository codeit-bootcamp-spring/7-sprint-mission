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
    private UUID uploadUserUuid;
    private String fileUrl;

    public BinaryContentIoDTO(UUID uuid, Instant uploadedAt, UUID uploadUserUuid, String fileUrl) {
        this.uuid = uuid;
        this.uploadedAt = uploadedAt;
        this.uploadUserUuid = uploadUserUuid;
        this.fileUrl = fileUrl;
    }
}
