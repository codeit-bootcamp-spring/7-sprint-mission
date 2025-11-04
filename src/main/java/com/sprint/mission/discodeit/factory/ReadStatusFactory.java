package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.dto.readstatus.request.ReadStatusCreateReq;
import com.sprint.mission.discodeit.entity.ReadStatus;

public class ReadStatusFactory {
    private ReadStatusFactory(){}

    public static ReadStatus create(ReadStatusCreateReq req){
        return ReadStatus.builder()
                .channelId(req.channelId())
                .userId(req.userId())
                .build();
    }
}
