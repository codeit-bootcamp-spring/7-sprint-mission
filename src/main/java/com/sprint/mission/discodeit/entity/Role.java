package com.sprint.mission.discodeit.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum Role {
    USER("USER"),
    CHANNEL_MANAGER("CHANNEL_MANAGER"),
    ADMIN("ADMIN");

    private final String value;

    Role(String value) { this.value = value; }

    @JsonCreator
    public static Role from(String source) {
        if (source == null || source.isBlank()) return USER;

        return Arrays.stream(values())
                .filter(role -> role.value.equalsIgnoreCase(source))
                .findFirst()
                .orElse(USER);
    }
}
