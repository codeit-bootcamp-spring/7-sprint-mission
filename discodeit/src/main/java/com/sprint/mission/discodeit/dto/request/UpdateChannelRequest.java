package com.sprint.mission.discodeit.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Getter @NoArgsConstructor @AllArgsConstructor
public class UpdateChannelRequest {
    private String name;
    private String description;
    public UUID channelId;
}
