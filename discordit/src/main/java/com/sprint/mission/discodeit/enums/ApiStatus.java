package com.sprint.mission.discodeit.enums;

import lombok.Getter;
import lombok.Value;
import org.springframework.stereotype.Component;

@Getter
public enum ApiStatus {

    SUCCESS("요청이 성공적으로 이루어졌습니다."),
    NO_DATA("요청이 성공적으로 이루어졌습니다."),
    INVALID_ARGS("잘못된 값입니다."),
    INVALID_USERNAME_LENGTH("사용자 이름 형식 오류");

    private final String description;

    ApiStatus(String description) {
        this.description = description;
    }
}
