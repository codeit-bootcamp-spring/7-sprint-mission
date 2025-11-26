package com.sprint.mission.discodeit.domain.exception;

import lombok.Getter;

@Getter
public enum ErrorType {
    INVALID_EMAIL("올바른 이메일 형식이 아닙니다."),
    INVALID_PASSWORD("비밀번호 형식이 올바르지 않습니다."),
    INVALID_PHONE_NUMBER("전화번호 형식이 올바르지 않습니다."),
    INVALID_USERNAME("사용자 이름을 입력해야 합니다.");

    private final String message;

    ErrorType(String message) {
        this.message = message;
    }
}
