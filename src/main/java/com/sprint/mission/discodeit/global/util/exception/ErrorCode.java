package com.sprint.mission.discodeit.global.util.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // USER
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    USERNAME_ALREADY_EXIST(HttpStatus.CONFLICT, "중복된 사용자 이름입니다."),
    EMAIL_ALREADY_EXIST(HttpStatus.CONFLICT, "중복된 이메일입니다."),
    LOGIN_USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "존재하지 않는 사용자입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 올바르지 않습니다."),


    // CHANNEL
    CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "채널을 찾을 수 없습니다."),
    PRIVATE_CHANNEL_UPDATE_NOT_ALLOWED(HttpStatus.FORBIDDEN, "Private 채널은 업데이트 할 수 없습니다."),

    // MESSAGE
    MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "메세지를 찾을 수 없습니다."),

    // READ_STATUS
    READ_STATUS_ALREADY_EXIST(HttpStatus.CONFLICT, "중복된 ReadStatus입니다."),
    READ_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "ReadStatus를 찾을 수 없습니다."),

    // BINARY_CONTENT
    BINARY_CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Binary Content를 찾을 수 없습니다."),

    //USER_STATUS
    USER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "UserStatus를 찾을 수 없습니다."),
    USER_STATUS_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 UserStatus입니다.");

    //

    private final HttpStatus status;
    private final String message;
}
