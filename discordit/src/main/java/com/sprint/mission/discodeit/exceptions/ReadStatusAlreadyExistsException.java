package com.sprint.mission.discodeit.exceptions;

import com.sprint.mission.discodeit.entity.ReadStatus;

public class ReadStatusAlreadyExistsException extends RuntimeException {
    public ReadStatusAlreadyExistsException(ReadStatus readStatus) {
        super(String.format("채널 %s와 유저 %s에 대한 ReadStatus가 이미 존재합니다.",
                readStatus.getChannel().getChannelName(),
                readStatus.getUser().getUserId()));
    }
}
