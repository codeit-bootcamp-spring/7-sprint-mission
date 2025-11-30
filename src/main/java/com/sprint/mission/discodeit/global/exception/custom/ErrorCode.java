package com.sprint.mission.discodeit.global.exception.custom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //===== USER =====//
    // 404 NOT FOUND
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),

    // 409 CONFLICT
    NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다."),

    //===== USER VALIDATOR =====//
    INVALID_NICKNAME_LENGTH(HttpStatus.BAD_REQUEST, "닉네임은 2자 이상 12자 이하로 입력해주세요."),
    INVALID_NICKNAME_PATTERN(HttpStatus.BAD_REQUEST, "닉네임에 특수문자는 사용할 수 없습니다. 다시 입력해주세요."),
    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "이메일 형식에 맞지 않습니다. 다시 입력하세요."),
    INVALID_PHONE_FORMAT(HttpStatus.BAD_REQUEST, "전화번호 형식에 맞지 않습니다. 다시 입력하세요."),
    INVALID_PASSWORD_FORMAT(HttpStatus.BAD_REQUEST, "비밀번호가 8자리보다 작거나 공백만 포함되어 있습니다. 다시 입력해주세요."),

    //===== CHANNEL =====//
    // 400 BAD REQUEST
    NOT_CHANNEL_MEMBER(HttpStatus.BAD_REQUEST, "사용자가 이 채널에 속해 있지 않습니다."),

    // 403 FORBIDDEN
    PRIVATE_CHANNEL_UPDATE_FORBIDDEN(HttpStatus.FORBIDDEN, "비공개 채널은 수정이 불가합니다."),

    // 404 NOT FOUND
    CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 채널을 찾을 수 없습니다."),

    // 409 CONFLICT
    CHANNEL_NAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "채널 이름이 이미 존재합니다. 다시 입력해주세요."),
    CHANNEL_MEMBER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 멤버가 채널에 속해 있습니다."),

    //===== MESSAGE =====//

    // 404 NOT FOUND
    MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "메시지가 존재하지 않습니다."),

    //===== AUTH =====//
    // 400 BAD REQUEST
    INVALID_LOGIN_REQUEST(HttpStatus.BAD_REQUEST, "아이디와 비밀번호를 모두 입력해야 합니다."),

    // 401 UNAUTHORIZED
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 일치하지 않습니다."),

    //===== USER_STATUS =====//
    // 400 BAD REQUEST
    USER_NOT_FOUND_FOR_STATUS(HttpStatus.BAD_REQUEST, "user status를 생성할 유저가 존재하지 않습니다."),

    // 404 NOT FOUND
    USER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "user status가 존재하지 않습니다."),

    // 409 CONFLICT
    USER_STATUS_ALREADY_EXISTS(HttpStatus.CONFLICT, "해당 유저의 user status가 이미 존재합니다."),

    //===== READ_STATUS =====//
    READSTATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "read status가 존재하지 않습니다."),

    //===== BINARY_CONTENT =====//
    BINARYCONTENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "파일이 존재하지 않습니다."),

    //==== NULL INPUT =====//
    NULL_INPUT_VALUE(HttpStatus.BAD_REQUEST, "null 값은 입력할 수 없습니다."),

    // 500 INTERNAL SERVER ERROR
    FILE_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 저장에 실패했습니다."),
    FILE_READ_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 읽기에 실패했습니다.");

    private final HttpStatus status;
    private final String message;
}
