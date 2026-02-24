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
    JWT_UNAUTHORIZED("🚨HTTPSTATUS_UNAUTHORIZED"),


    // Security 관련 에러 코드
    INVALID_TOKEN("🚨토큰이 유효하지 않습니다."),
    INVALID_USER_DETAILS("🚨사용자 인증 정보(UserDetails)가 유효하지 않습니다."),

    ERROR_RESPONSE_401("🚨인증되지 않은 요청"),
    ERROR_RESPONSE_403("🚨인가되지 않은 요청 - 요청자 본인의 알림에 대해서만 수행할 수 있습니다."),
    ERROR_RESPONSE_404("🚨Zero Notification");


    private final String message;
}
