package com.sprint.mission.discodeit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND("🚨USER_NOT_FOUND"),
    USERSTATUS_NOT_FOUND("🚨USERSTATUS_NOT_FOUND"),
    READSTATUS_NOT_FOUND("🚨READSTATUS_NOT_FOUND"),
    CHANNEL_NOT_FOUND("🚨CHANNEL_NOT_FOUND"),
    MESSAGE_NOT_FOUND("🚨USER_NOT_FOUND"),

    DUPLICATE_READSTATUS("🚨DUPLICATE_READSTATUS"),
    DUPLICATE_USER("🚨DUPLICATE_USER"),
    PRIVATE_CHANNEL_UPDATE("🚨PRIVATE_CHANNEL_UPDATE"),

    ILLEAGALARGUEMNTEXCEPTION("🚨IllegalArgumentException"),

    // 500 SERVER ERROR
    INTERNAL_SERVER_ERROR("🚨서버 오류"),

    // JWT.UnAuthorizedErr
    JWT_UNAUTHORIZED("🚨HTTPSTATUS_UNAUTHORIZED");

    private final String message;
}
