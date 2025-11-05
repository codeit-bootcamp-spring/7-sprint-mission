package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.entity.ReadStatus;

public class ReadStatusFactory {
    private ReadStatusFactory(){}

    public static ReadStatus create(ReadStatus readStatus){
        return new ReadStatus(
                readStatus.getUserId(),
                readStatus.getChannelId()
        );
    }
}
