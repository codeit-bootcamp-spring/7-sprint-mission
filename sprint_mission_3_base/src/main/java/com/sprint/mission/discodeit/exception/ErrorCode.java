package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND("User not found."),
    DUPLICATE_USER("User already exists."),
    CHANNEL_NOT_FOUND("Channel not found."),
    PRIVATE_CHANNEL_UPDATE("Private channel cannot be updated."),
    MESSAGE_NOT_FOUND("Message not found."),
    BINARY_CONTENT_NOT_FOUND("Binary content not found."),
    INVALID_CREDENTIALS("Invalid credentials."),
    VALIDATION_FAILED("Validation failed."),
    BAD_REQUEST("Bad request."),
    INTERNAL_ERROR("Unexpected error occurred.");

    private final String message;

    public String getMessage() {
        return message;
    }
}
