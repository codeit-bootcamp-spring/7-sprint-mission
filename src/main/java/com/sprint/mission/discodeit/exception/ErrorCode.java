package com.sprint.mission.discodeit.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND("해당 유저를 찾을 수 없습니다."),
    DUPLICATE_USER("이미 존재하는 유저입니다."),
    CHANNEL_NOT_FOUND("해당 채널을 찾을 수 없습니다."),
    PRIVATE_CHANNEL_UPDATE("DM은 수정할 수 없습니다."),
    MESSAGE_NOT_FOUND("해당 메세지를 찾을 수 없습니다."),
    NOTIFICATION_NOT_FOUND("해당 알림을 찾을 수 없습니다."),
    DUPLICATE_READ_STATUS("이미 참가중인 채널입니다."),
    READ_STATUS_NOT_FOUND("해당 채널 수신 정보를 찾을 수 없습니다."),
    LOGIN_PASSWORD("비밀번호가 일치하지 않습니다."),
    INVALID_TOKEN("유효하지 않은 토큰입니다."),
    BINARY_CONTENT_NOT_FOUND("해당 파일을 찾을 수 없습니다.");



    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
