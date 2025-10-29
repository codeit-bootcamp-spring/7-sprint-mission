package com.sprint.mission.discodeit.domain.exception;

public class CannotSendFriendRequestToSelfException extends RuntimeException{
    public CannotSendFriendRequestToSelfException() {
        super("자기 자신에게는 친구 요청을 보낼 수 없습니다.");
    }
}
