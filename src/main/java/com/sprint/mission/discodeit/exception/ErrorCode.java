package com.sprint.mission.discodeit.exception;

public enum ErrorCode {

    USER_NOT_FOUND("User Not Found"),
    DUPLICATE_USER("Duplicate User"),
    CHANNEL_NOT_FOUND("Channel Not Found"),
    PRIVATE_CHANNEL_UPDATE("Private Channel Update Not Allowed"),;

    String message;
    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
