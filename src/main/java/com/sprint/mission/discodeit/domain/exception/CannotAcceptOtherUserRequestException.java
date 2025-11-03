package com.sprint.mission.discodeit.domain.exception;

public class CannotAcceptOtherUserRequestException extends RuntimeException{
    public CannotAcceptOtherUserRequestException() {
        super("자신에게 온 친구 요청이 아니면 수락할 수 없습니다.");
    }
}
