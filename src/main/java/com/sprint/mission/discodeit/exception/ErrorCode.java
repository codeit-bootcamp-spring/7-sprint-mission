package com.sprint.mission.discodeit.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND("생성 실패"),
    DUPLICATE_USER("실패"),
    CHANNEL_NOT_FOUND("실패"),
    PRIVATE_CHANNEL_UPDATE("실패");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
