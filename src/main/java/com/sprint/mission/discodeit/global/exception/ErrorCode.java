package com.sprint.mission.discodeit.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //===== USER =====//
    // 404 NOT FOUND
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),

    // 409 CONFLICT
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다."),

    //===== CHANNEL =====//
    // 400 BAD REQUEST
    NOT_CHANNEL_MEMBER(HttpStatus.BAD_REQUEST, "사용자가 채널에 속해 있지 않습니다."),

    // 403 FORBIDDEN
    PRIVATE_CHANNEL_UPDATE_FORBIDDEN(HttpStatus.FORBIDDEN, "비공개 채널은 수정할 수 없습니다."),

    // 404 NOT FOUND
    CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "채널을 찾을 수 없습니다."),

    // 409 CONFLICT
    CHANNEL_NAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 채널 이름 입니다."),
    CHANNEL_MEMBER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 채널에 속해있는 멤버 입니다."),

    //===== MESSAGE =====//

    // 404 NOT FOUND
    MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "메시지를 찾을 수 없습니다."),

    //===== AUTH =====//
    // 400 BAD REQUEST
    INVALID_LOGIN_REQUEST(HttpStatus.BAD_REQUEST, "아이디와 비밀번호를 모두 입력해야 합니다."),

    // 401 UNAUTHORIZED
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 일치하지 않습니다."),

    //===== USER_STATUS =====//
    // 400 BAD REQUEST
    USER_NOT_FOUND_FOR_USER_STATUS(HttpStatus.BAD_REQUEST, "user status를 생성할 사용자가 존재하지 않습니다."),

    // 404 NOT FOUND
    USER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "user status를 찾을 수 없습니다."),

    // 409 CONFLICT
    USER_STATUS_ALREADY_EXISTS(HttpStatus.CONFLICT, "해당 사용자의 user status가 이미 존재합니다."),

    //===== READ_STATUS =====//
    READSTATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "read status를 찾을 수 없습니다."),

    //===== BINARY_CONTENT =====//
    BINARYCONTENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "파일을 찾을 수 없습니다."),

    // 400 Bad Request
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "요청 데이터가 유효하지 않습니다."),

    // 500 INTERNAL SERVER ERROR
    FILE_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 저장 중 오류가 발생했습니다."),
    FILE_READ_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 읽기 중 오류가 발생했습니다."),

    UNHANDLED_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "처리되지 않은 예외가 발생했습니다."),

    //===== JWT =====//
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED,"토큰이 만료되었습니다. 다시 로그인해주세요."),
    UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST,"지원되지 않는 토큰 형식입니다."),
    MALFORMED_TOKEN(HttpStatus.BAD_REQUEST,"잘못된 토큰 형식입니다."),
    INVALID_SIGNATURE(HttpStatus.FORBIDDEN,"토큰 서명 검증에 실패했습니다."),
    EMPTY_OR_INVALID_TOKEN(HttpStatus.BAD_REQUEST,"토큰이 비어 있거나 유효하지 않습니다.");

    private final HttpStatus status;
    private final String message;
}
