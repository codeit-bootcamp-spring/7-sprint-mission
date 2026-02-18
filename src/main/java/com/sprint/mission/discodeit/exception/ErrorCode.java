package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 공통
    INVALID_INPUT("입력값이 잘못되었습니다.", HttpStatus.BAD_REQUEST, "CM-001"),
    NOT_FOUND("리소스를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, "CM-002"),
    CONFLICT("이미 존재합니다.", HttpStatus.CONFLICT, "CM-003"),
    INVALID_STATE("요청을 처리할 수 없는 상태입니다.", HttpStatus.CONFLICT, "CM-004"),
    INTERNAL_SERVER_ERROR("", HttpStatus.INTERNAL_SERVER_ERROR, "CM-005"),
    UNAUTHORIZED("인증 실패하였습니다.", HttpStatus.UNAUTHORIZED, "CM-006"),
    FORBIDDEN("권한이 없습니다.", HttpStatus.FORBIDDEN, "CM-007"),
    INVALID_TOKEN("유효하지 않은 토큰입니다.",HttpStatus.UNAUTHORIZED, "CM-008" ),
    EXPIRED_TOKEN("만료된 토큰입니다", HttpStatus.UNAUTHORIZED , "CM-009" ),

    // User
    USER_NOT_FOUND("유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, "U-001"),
    DUPLICATE_USER("이미 존재하는 유저 입니다.", HttpStatus.CONFLICT, "U-002"),

    // Channel
    CHANNEL_NOT_FOUND("채널을 찾을수 없습니다.", HttpStatus.NOT_FOUND, "CH-001"),
    CHANNEL_ACCESS_DENIED("채널 권한이 없습니다.", HttpStatus.FORBIDDEN, "CH-002"),
    CHANNEL_MINIMUM_MEMBERS_NOT_MET("PRIVATE 채널 최소 2명 이상이어야 합니다.", HttpStatus.BAD_REQUEST, "CH-003"),
    CHANNEL_INVALID_PARTICIPANTS("참여 유저가 잘못되었습니다.", HttpStatus.BAD_REQUEST, "CH-004"),
    CHANNEL_MODIFICATION_NOT_ALLOWED("해당 채널은 수정할 수 없습니다.", HttpStatus.CONFLICT, "CH-005"),
    UNSUPPORTED_CHANNEL_TYPE("지원하지 않는 채널 타입입니다.", HttpStatus.BAD_REQUEST, "CH-006"),
    CHANNEL_ALREADY_JOINED("이미 참여한 유저입니다.", HttpStatus.CONFLICT, "CH-007"),
    CHANNEL_NOT_JOINED("채널에 참여하지 않은 사용자입니다.", HttpStatus.CONFLICT, "CH-008"),

    // Message
    MESSAGE_NOT_FOUND("메세지가 없습니다.", HttpStatus.NOT_FOUND, "M-001"),
    MESSAGE_SEND_NOT_ALLOWED("채널 맴버만 메세지 전송 가능합니다.", HttpStatus.FORBIDDEN, "M-002"),

    // Auth
    INVALID_CREDENTIALS("이름 또는 비밀번호가 잘못되었습니다.", HttpStatus.UNAUTHORIZED, "AU-001"),

    // BinaryContent
    BINARY_CONTENT_NOT_FOUND("해당 파일을 찾을수 없습니다.", HttpStatus.NOT_FOUND, "BC-001"),

    // ReadStatus
    READ_STATUS_DUPLICATED("이미 ReadStatus가 존재합니다.", HttpStatus.CONFLICT, "RS-001"),
    READ_STATUS_NOT_FOUND("읽음 상태가 존재하지 않습니다.", HttpStatus.NOT_FOUND, "RS-002"),
    // ReadStatus
    READ_STATUS_CREATE_NOT_ALLOWED("해당 채널 타입에서는 ReadStatus를 생성할 수 없습니다.", HttpStatus.CONFLICT, "RS-003"),

    // UserStatus
    USER_STATUS_NOT_FOUND("유저 상태를 찾을 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR, "US-001"),
    USER_STATUS_ALREADY_EXISTS("해당 유저의 상태는 이미 등록되었습니다.", HttpStatus.CONFLICT, "US-002");


    private final String message;
    private final HttpStatus status;
    private final String code;

    ErrorCode(String message, HttpStatus status, String code) {
        this.message = message;
        this.status = status;
        this.code = code;
    }

}
