package com.sprint.mission.discodeit.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record Dto_CreateChannelPrivate( //all private final
        @NotBlank(message = "Channel name is mandatory")
        List<UUID> userIDs // PRIVATE 일 경우 사용
) {
    public static Dto_CreateChannelPrivate from(List<UUID> userIDs) {
//        [ ] name과 description 속성은 생략합니다.
        return Dto_CreateChannelPrivate.builder()
                .userIDs(userIDs)
                .build();
    }
}
