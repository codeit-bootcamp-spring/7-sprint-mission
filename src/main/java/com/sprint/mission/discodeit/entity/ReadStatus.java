package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
