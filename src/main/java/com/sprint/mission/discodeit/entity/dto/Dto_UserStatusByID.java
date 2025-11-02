package com.sprint.mission.discodeit.entity.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record Dto_UserStatusByID(
//        UUID id,
//        Instant createdAt,
//        Instant updatedAt,

        UUID userId) {
//        boolean isOnline) {
    public static Dto_UserStatusByID from(UUID userId) {
        return Dto_UserStatusByID.builder()
//                .id(userId)
//                .createdAt()
//                .updatedAt(Instant.now())
                .userId(userId)
//                .isOnline(isOnline)
                .build();
    }
}
