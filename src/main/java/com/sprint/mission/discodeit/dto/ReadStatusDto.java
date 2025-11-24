package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.time.Instant;
import java.util.UUID;


// 🚫 편의상 변경. CamelCase 위반 -> 추후 수정 할꺼예용~
public record ReadStatusDto( //all private final
                             //@NotBlank(message = "id is mandatory")
                             UUID id,
//                             Instant createdAt,
//                             Instant updatedAt,
                             //@NotBlank(message = "id is mandatory")
                             UUID userId,
                             //@NotBlank(message = "channelId is mandatory")
                             UUID channelId,
                             Instant lastReadAt
) {
    public static ReadStatusDto from(ReadStatus readStatus) {
        return new ReadStatusDto(readStatus.getId()
//            , readStatus.getCreatedAt()
//            , readStatus.getUpdatedAt()
            , readStatus.getUserId()
            , readStatus.getChannelId()
            , readStatus.getLastReadAt());
    }
}
