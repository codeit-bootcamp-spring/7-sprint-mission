package com.sprint.mission.discodeit.dto.fileIo;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * 파일 IO용 ReadStatus DTO
 */
@Getter
public class ReadStatusIoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID uuid;
    private UUID userUuid;
    private UUID channelUuid;
    private Instant lastReadAt;

    public ReadStatusIoDTO(UUID uuid, UUID userUuid, UUID channelUuid, Instant lastReadAt) {
        this.uuid = uuid;
        this.userUuid = userUuid;
        this.channelUuid = channelUuid;
        this.lastReadAt = lastReadAt;
    }
}

