package com.sprint.mission.discodeit.DTO;

import com.sprint.mission.discodeit.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ChannelDTO {
    private String serverName;
    private Long serverLevel;
    private boolean isPrivate;

}
