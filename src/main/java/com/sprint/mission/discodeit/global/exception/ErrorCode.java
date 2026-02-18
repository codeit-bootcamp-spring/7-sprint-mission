package com.sprint.mission.discodeit.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // USER
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "중복된 사용자입니다."),

    //AUTH
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "로그인 요청이 실패하였습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    // CHANNEL
    CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "채널을 찾을 수 없습니다."),
    PRIVATE_CHANNEL_UPDATE_NOT_ALLOWED(HttpStatus.FORBIDDEN,
            "Private 채널은 업데이트 할 수 없습니다."),

    // MESSAGE
    MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "메세지를 찾을 수 없습니다."),

    // READ_STATUS
    READ_STATUS_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "중복된 ReadStatus입니다."),
    READ_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "ReadStatus를 찾을 수 없습니다."),

    // BINARY_CONTENT
    BINARY_CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Binary Content를 찾을 수 없습니다."),

    // INTERNAL
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),

    // VALID
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "유효성 검사에 실패하였습니다."),

    // AUTH & Security
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");

    private final HttpStatus status;
    private final String message;
}
