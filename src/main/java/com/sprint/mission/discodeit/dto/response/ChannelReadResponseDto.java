package com.sprint.mission.discodeit.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@ToString
public class ChannelReadResponseDto {

    private Instant recentPostTime;
    private String name;
    private String description;
    private boolean isTextChannel;
    private boolean isPublic;
    @Builder.Default
    private HashSet<UUID> userIdList  = new HashSet<>();


}
