package com.sprint.mission.discodeit.entity.dto;

import lombok.Builder;

@Builder
public record Dto_AuthService(
        String userName,
        String password
) {
    public static Dto_AuthService from(String userName, String password) {
        return Dto_AuthService.builder()
                .userName(userName)
                .password(password)
                .build();
    }
}
