package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.enums.ChannelType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Channel extends Common {
    @Serial
    private static final long serialVersionUID = 1L;
    private ChannelType type;
    private String channelName;
    private String desc;

    @ToString.Exclude
    private List<User> participants = new ArrayList<>();


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

    // 유저 추가
    public void addParticipant(User user) {
        if (user != null && !participants.contains(user)) {
            participants.add(user);
            touch();
        }
    }

    // 유저 제거
    public void removeParticipant(User user) {
        if (user != null && participants.remove(user)) {
            participants.remove(user);
            touch();
        }
    }





}
