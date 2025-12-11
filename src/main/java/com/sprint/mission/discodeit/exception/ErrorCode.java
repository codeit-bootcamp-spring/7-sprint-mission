package com.sprint.mission.discodeit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "사용자를 찾을수 없습니다"),
    DUPLICATE_USER_NAME(HttpStatus.CONFLICT, "U002", "이미 존재하는 이름입니다."),
    DUPLICATE_USER_EMAIL(HttpStatus.CONFLICT, "U003", "이미 존재하는 이메일입니다."),

    // Channel
    CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "C001", "채널을 찾을 수 없습니다."),

    // Message
    MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "메시지를 찾을 수 없습니다."),


    // BinaryContent
    DIRECTORY_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "B001", "디렉토리 생성에 실패했습니다."),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "B002", "파일을 찾을 수 없습니다."),
    FILE_OPERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "B003", "파일 처리 중 오류가 발생했습니다."),
    FILE_SIZE_LIMIT_EXCEED(HttpStatus.INTERNAL_SERVER_ERROR, "B003", "파일 처리 중 오류가 발생했습니다."),

    // ReadStatus

    // UserStatus

    // Auth
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "A001", "아이디 또는 비밀번호가 틀렸습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
