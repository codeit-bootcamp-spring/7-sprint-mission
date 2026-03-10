package com.sprint.mission.discodeit.service.basic.sse;

import lombok.Getter;

@Getter
public enum SseEventType {
    CREATED("created"),
    UPDATED("updated"),
    DELETED("deleted");

    private final String value;

    SseEventType(String value) {
        this.value = value;
    }

}
