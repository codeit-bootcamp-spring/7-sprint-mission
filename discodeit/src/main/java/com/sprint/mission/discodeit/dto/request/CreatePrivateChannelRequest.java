package com.sprint.mission.discodeit.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter @AllArgsConstructor @NoArgsConstructor
public class CreatePrivateChannelRequest {
    private List<UUID> userIds;
}
