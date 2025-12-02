package com.sprint.mission.discodeit.service.dto.response;

import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class ChannelDto {

    private UUID id;
    private String name;
    private String description;
    private ChannelType type;
    private List<UserDto> participants = new ArrayList<>();
    private Instant lastMessageAt;

}