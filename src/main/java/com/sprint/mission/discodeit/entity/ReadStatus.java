package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.dto.Dto_ReadStatusUpdate;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus extends BaseModel {
//    사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현하는 도메인 모델입니다. 사용자별 각 채널에 읽지 않은 메시지를 확인하기 위해 활용합니다.
    private UUID userId;
    private UUID channelId;
    private Instant lastReadAt;

    public ReadStatus(UUID userId, UUID channelId) {
        super();
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = Instant.now();
    }

    public void updateLastReadAt(Dto_ReadStatusUpdate requestDto) {
        this.lastReadAt = requestDto.newLastReadAt(); // Instant.now();
    }

    @Override
    public String toString() {
        return "ReadStatus = "
            + super.toString()
            + "userId = [" + userId + "]\n"
            + "channelId = [" + channelId + "]\n"
            + "lastReadAt = [" + lastReadAt + "]";
    }
}
