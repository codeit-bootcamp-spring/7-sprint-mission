package com.sprint.mission.discodeit.entity.status;

import com.sprint.mission.discodeit.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ReadStatus extends BaseEntity {

    private UUID userId;
    private UUID channelId;

    public void updateReadStatus() {
        updateTimestamp();
    }
}
