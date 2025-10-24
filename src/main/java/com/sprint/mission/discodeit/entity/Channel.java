package com.sprint.mission.discodeit.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Channel extends Common {
    @Serial
    private static final long serialVersionUID = 1L;
    private ChannelType type;
    private String channelName;
    private String desc;

    public Channel(ChannelType type, String channelName, String desc) {
        this.type = type;
        this.channelName = channelName;
        this.desc = desc;
    }

    public void updateChannel(ChannelType type, String channelName, String desc) {
        if(type != null) this.type = type;
        if(channelName != null) this.channelName = channelName;
        if(desc != null) this.desc = desc;
    }


}
