package com.sprint.mission.discodeit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "사용자를 찾을 수 없습니다."),
    DUPLICATE_USER_NAME(HttpStatus.CONFLICT, "U002", "이미 존재하는 이름입니다."),
    DUPLICATE_USER_EMAIL(HttpStatus.CONFLICT, "U003", "이미 존재하는 이메일입니다."),

    // Channel
    CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "C001", "채널을 찾을 수 없습니다."),
    PRIVATE_CHANNEL_UPDATE(HttpStatus.FORBIDDEN, "C002", "프라이빗 채널은 수정할 수 없습니다."),

    // Message
    MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "메시지를 찾을 수 없습니다."),
    MESSAGE_NOT_EMPTY(HttpStatus.UNPROCESSABLE_ENTITY, "M002", "메시지는 비어있을 수 없습니다."),

    // BinaryContent
    DIRECTORY_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "B001", "디렉토리 생성에 실패했습니다."),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "B002", "파일을 찾을 수 없습니다."),
    FILE_OPERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "B003", "파일 처리 중 오류가 발생했습니다."),
    FILE_SIZE_LIMIT_EXCEED(HttpStatus.PAYLOAD_TOO_LARGE, "B004", "파일 업로드 최대 크기를 초과했습니다."),
    FILE_UPLOAD_LIMIT_EXCEED(HttpStatus.UNPROCESSABLE_ENTITY, "B005", "업로드는 한 번에 10건만 가능합니다."),

    // ReadStatus
    READ_NOT_FOUND(HttpStatus.NOT_FOUND, "R001", "수신 정보를 찾을 수 없습니다."),
    READ_ALREADY_EXISTS(HttpStatus.CONFLICT, "R002", "수신 정보가 이미 존재합니다."),

    // UserStatus
    STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "S001", "상태 정보를 찾을 수 없습니다."),
    STATUS_ALREADY_EXISTS(HttpStatus.CONFLICT, "S002", "상태 정보가 이미 존재합니다."),

    // Auth
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "A001", "아이디 또는 비밀번호가 틀렸습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "A002", "권한이 부족합니다"),
    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "A003", "로그인이 필요합니다."),

    // Token
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "T001", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "T002", "만료된 토큰입니다."),

    //Notification
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "N001", "알람을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
