package com.sprint.mission.discodeit.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 400 Bad Request
    INVALID_ARGS("잘못된 값입니다.", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME_LENGTH("사용자 이름 형식 오류", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST_BODY("요청 본문을 읽을 수 없습니다.", HttpStatus.BAD_REQUEST),
    ILLEGAL_ARGUMENT("잘못된 인자입니다.", HttpStatus.BAD_REQUEST),
    ALREADY_EXISTS("이미 존재하는 값입니다.", HttpStatus.BAD_REQUEST),

    // 404 Not Found
    NOT_FOUND("존재하지 않는 값입니다.", HttpStatus.NOT_FOUND),

    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR("서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String description;
    private final HttpStatus httpStatus;

    ErrorCode(String description, HttpStatus httpStatus) {
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
