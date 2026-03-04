package com.sprint.mission.discodeit.common.exception;

import lombok.Getter;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"사용자를 찾을 수 없습니다."),
    DUPLICATE_USER(HttpStatus.CONFLICT,"이미 존재하는 사용자입니다."),
    INVALID_USER_REQUEST(HttpStatus.BAD_REQUEST,"유효하지 않은 요청입니다."),

    // Channel
    CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND,"채널을 찾을 수 없습니다."),
    PRIVATE_CHANNEL_UPDATE(HttpStatus.BAD_REQUEST,"PRIVATE 채널은 수정할 수 없습니다."),
    CHANNEL_MEMBER_NOT_FOUND(HttpStatus.FORBIDDEN,"채널의 멤버를 찾을 수 없습니다."),
    INVALID_CHANNEL_REQUEST(HttpStatus.BAD_REQUEST, "유효하지 않은 요청입니다."),

    // Message
    MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND,"메세지를 찾을 수 없습니다."),
    SLOW_MODE_VIOLATION(HttpStatus.BAD_REQUEST,"슬로우 모드 제한으로 메세지를 보낼 수 없습니다."),
    INVALID_SLOW_MODE(HttpStatus.BAD_REQUEST,"올바르지 않은 슬로우 모드 값입니다."),
    INVALID_MESSAGE_REQUEST(HttpStatus.BAD_REQUEST,"유효하지 않은 요청입니다."),

    // BinaryContent
    BINARY_CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND,"파일을 찾을 수 없습니다."),
    INVALID_FILE(HttpStatus.BAD_REQUEST,"유효하지 않은 파일입니다."),
    INVALID_BINARY_CONTENT_REQUEST(HttpStatus.BAD_REQUEST,"요청한 내용이 잘못되었습니다."),
    BINARY_CONTENT_STORAGE_PUT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"파일 저장에 실패하였습니다."),
    BINARY_CONTENT_STORAGE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"파일 삭제에 실패하였습니다."),
    BINARY_CONTENT_STORAGE_DOWNLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"파일 다운로드에 실패하였습니다."),

    // ReadStatus
    READ_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND,"읽음 상태를 찾을 수 없습니다."),
    READ_STATUS_ALREADY_EXISTS(HttpStatus.CONFLICT,"읽음 상태가 이미 존재합니다."),
    INVALID_READ_STATUS_REQUEST(HttpStatus.BAD_REQUEST,"유효하지 않은 요청입니다."),

    // UserStatus
    USER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND,"사용자 상태를 찾을 수 없습니다."),
    USER_STATUS_ALREADY_EXISTS(HttpStatus.CONFLICT,"사용자 상태가 이미 존재합니다."),
    INVALID_USER_STATUS_REQUEST(HttpStatus.BAD_REQUEST,"유효하지 않은 요청입니다."),

    // Auth
    AUTH_INVALID_REQUEST(HttpStatus.BAD_REQUEST,"로그인 요청이 올바르지 않습니다."),
    AUTH_USER_NOT_FOUND(HttpStatus.NOT_FOUND,"로그인 사용자를 찾을 수 없습니다."),
    AUTH_WRONG_PASSWORD(HttpStatus.BAD_REQUEST,"비밀번호가 올바르지 않습니다."),
    AUTH_ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    AUTH_ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 권한이 없습니다."),
    AUTH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "토큰을 찾을 수 없습니다."),

    // Notification
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "알림을 찾을 수 없습니다."),
    NOTIFICATION_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    // common
    INVALID_REQUEST(HttpStatus.BAD_REQUEST,"요청이 올바르지 않습니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED,"신뢰할 수 없습니다."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"서버 내부 오류가 발생했습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다.");

    private final String message;
    private final HttpStatus status;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
