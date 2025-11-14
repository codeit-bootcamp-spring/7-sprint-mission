package com.sprint.mission.discodeit.entity.dto;

import com.sprint.mission.discodeit.entity.ReadStatus;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.UUID;


// 🚫 편의상 변경. CamelCase 위반 -> 추후 수정 할꺼예용~
public record Res_ReadStatus( //all private final
    //@NotBlank(message = "id is mandatory")
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    //@NotBlank(message = "id is mandatory")
    UUID userId,
    //@NotBlank(message = "channelId is mandatory")
    UUID channelId,
    Instant lastReadAt
) {
    public static Res_ReadStatus from(ReadStatus readStatus) {
        return new Res_ReadStatus(readStatus.getId()
            , readStatus.getCreatedAt()
            , readStatus.getUpdatedAt()
            , readStatus.getId()
            , readStatus.getChannelId()
            , readStatus.getLastReadAt());
    }
}
