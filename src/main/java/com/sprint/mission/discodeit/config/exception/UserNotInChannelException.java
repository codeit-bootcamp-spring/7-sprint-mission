package com.sprint.mission.discodeit.config.exception;

import com.sprint.mission.discodeit.participation.ParticipationDualKey;

public class UserNotInChannelException extends SecurityException{
    public UserNotInChannelException(ParticipationDualKey participationId) {
        super(participationId.channelId()+"에서 " + participationId.authorId()+"는 참여자가 아닙니다.");
    }

    public UserNotInChannelException(String message) {
        super(message);
    }
}
