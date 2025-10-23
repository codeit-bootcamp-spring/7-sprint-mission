package com.sprint.mission.discodeit.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

@Getter @Setter //Setter의 경우 추후 수정 가능성
@ToString
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


    public enum ChannelType {
        PUBLIC, PRIVATE
    }

}
