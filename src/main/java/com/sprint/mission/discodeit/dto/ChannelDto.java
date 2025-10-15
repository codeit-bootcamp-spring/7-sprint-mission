package com.sprint.mission.discodeit.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelDto {
    private String serverName;
    private Long serverLevel;
    private boolean isPrivate;

}
