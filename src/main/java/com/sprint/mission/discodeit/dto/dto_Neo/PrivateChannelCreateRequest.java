package com.sprint.mission.discodeit.dto.dto_Neo;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;


public record PrivateChannelCreateRequest( //all private final
    @NotEmpty
    List<UUID> participantIds // PRIVATE 일 경우 사용
) {
    public static PrivateChannelCreateRequest from(List<UUID> userIDs) {
        return new PrivateChannelCreateRequest(userIDs);
    }
}
